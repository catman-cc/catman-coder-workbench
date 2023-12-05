package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import org.bson.Document;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascade;
import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascaded;
import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.CascadedTag;
import lombok.Builder;
import lombok.Data;

public class WriteCascadeProcessor implements ICascadeProcessor {
    final private ExpressionParser expressionParser;

    final private EvaluationContext context;

    final private CascadeHelper cascadeHelper;

    final private MongoOperations mongoOperations;
    final private Cascade cascade;

    final private String[] when;

    final private CascadeFillType cascadeFillType;

    private final CascadeType cascadeType;

    Function<Object, Object> func;

    public WriteCascadeProcessor(CascadeHelper cascadeHelper, ExpressionParser expressionParser,
            EvaluationContext context, MongoOperations mongoOperations, Cascade cascade, Function<Object, Object> func,
            CascadeType cascadeType) {
        this.cascadeHelper = cascadeHelper;
        this.context = context;
        this.cascade = cascade;
        this.when = cascade.when();
        this.cascadeFillType = cascade.fillType();

        this.expressionParser = expressionParser;
        this.mongoOperations = mongoOperations;
        this.func = func;

        this.cascadeType = cascadeType;
    }

    @SuppressWarnings("unchecked")
    public ProcessResult handler(Object obj, Object document) {
        if (obj.getClass().isArray()) {
            Object[] arr = (Object[]) obj;
            return handler(arr, (List<Object>) document);
        } else if (obj instanceof Collection<?> cs) {
            return handler(cs, (List<Object>) document);
        } else if (obj instanceof Map<?, ?> map) {
            return handler(map, (Document) document);
        } else {
            // 普通实例对象,此时一样需要replace document
            return doProcess(obj, document);

        }
    }

    private ProcessResult doProcess(Object obj, Object document) {
        Optional<org.springframework.data.mongodb.core.mapping.Document> de = Optional.ofNullable(AnnotationUtils
                .findAnnotation(obj.getClass(), org.springframework.data.mongodb.core.mapping.Document.class));
        Optional<CascadedTag> ce = Optional
                .ofNullable(AnnotationUtils.findAnnotation(obj.getClass(), CascadedTag.class));
        return Optional.ofNullable(obj)
                .filter(o -> de.isPresent()
                        || ce.isPresent())
                .filter(d -> {
                    context.setVariable("each", obj);
                    return Arrays.stream(when)
                            .allMatch(w -> expressionParser.parseExpression(w).getValue(context, Boolean.class));
                })
                .filter(d -> Optional.ofNullable(AnnotationUtils.findAnnotation(obj.getClass(), Cascaded.class))
                        .map(cascded -> {
                            // 如果当前是删除操作,那么将评估是否需要执行删除操作
                            return CascadeType.DELETE.equals(cascadeType)
                                    && Arrays.stream(cascded.value()).noneMatch(item -> {
                                        String query = expressionParser.parseExpression(item.query()).getValue(context,
                                                String.class);
                                        long count = mongoOperations
                                                .count(new BasicQuery(query), item.entity(),
                                                        StringUtils.hasText(item.collection())
                                                                ? item.collection()
                                                                : mongoOperations.getCollectionName(item.entity()));
                                        long exclude = 0;
                                        if (StringUtils.hasText(item.excludeQuery())) {
                                            String ex = expressionParser.parseExpression(item.query()).getValue(context,
                                                    String.class);
                                            exclude = mongoOperations
                                                    .count(new BasicQuery(ex), item.entity(),
                                                            StringUtils.hasText(item.collection())
                                                                    ? item.collection()
                                                                    : mongoOperations.getCollectionName(item.entity()));
                                        }
                                        return count - exclude <= item.minCount();
                                    });
                        }).orElse(true))
                .map(d -> {
                    // 这里需要进行特殊处理,因为想要处理的对象不一定是
                    if (de.isPresent()) {
                        // 如果是有mongo标记的情况下,需要直接进行存储操作
                        Object res = func.apply(obj);
                        // 转换为文档数据
                        CascadeInfo info = CascadeInfo.builder()
                                .id(cascadeHelper.findIdValue(res))
                                .collection(cascadeHelper.findCollectionsName(res.getClass()))
                                ._class(obj.getClass())
                                .build();
                        ProcessResult result = ProcessResult.builder()
                                .object(res)
                                .document(info.toDocument())
                                .change(true)
                                .build();
                        return result;
                    }

                    Document cd = (Document) document;
                    AtomicBoolean change = new AtomicBoolean(false);
                    // 如果只是标记了CascadedTag,那么原始对象是不需要存储的,还需要递归处理其内部字段
                    ReflectionUtils.doWithFields(obj.getClass(), field -> {
                        ReflectionUtils.makeAccessible(field);
                        Optional.ofNullable(ReflectionUtils.getField(field, obj)).ifPresent(fv -> {
                            String fieldName = cascadeHelper.findFieldName(field);
                            Object doc = cd.get(fieldName);
                            ProcessResult handler = handler(fv, doc);
                            if (handler.change) {
                                change.set(true);
                                if (isFillChange()) {
                                    ReflectionUtils.setField(field, obj, handler.object);
                                }
                                cd.replace(fieldName, handler.document);
                            } else if (!isFileUnChange()) {
                                ReflectionUtils.setField(field, obj, null);
                            }
                        });

                    });

                    // 最普通的对象实例
                    return ProcessResult.builder()
                            .change(change.get())
                            .object(obj)
                            .document(document)
                            .build();

                }).orElseGet(() -> ProcessResult.builder()
                        .object(obj)
                        .document(document)
                        .build());
    }

    public ProcessResult handler(Collection<?> objs, List<Object> documents) {
        List<Object> objects = new ArrayList<>();
        int i = 0;
        boolean change = false;
        for (Object obj : objs) {
            ProcessResult handler = handler(obj, documents.get(i));
            if (handler.change) {
                change = true;
                if (isFillChange()) {
                    objects.add(handler.object);
                }
                documents.remove(i);
                documents.add(i, handler.getDocument());
            } else {
                if (isFileUnChange()) {
                    objects.add(obj);
                }
            }
            i++;
        }
        return ProcessResult.builder()
                .change(change)
                .object(objects.toArray())
                .document(documents)
                .build();
    }

    @SuppressWarnings("unchecked")
    public ProcessResult handler(Object[] objs, List<Object> documents) {
        ProcessResult result = handler(Arrays.asList(objs), documents);
        List<Object> objects = (List<Object>) result.object;
        return ProcessResult.builder()
                .object(objects.toArray())
                .document(result.document)
                .build();
    }

    public ProcessResult handler(Map<?, ?> map, Document document) {
        // 需要处理的是一个map集合
        Map<Object, Object> res = new HashMap<>();
        AtomicBoolean change=new AtomicBoolean(false);
        new HashMap<>(map).forEach((k, v) -> {
            if (k instanceof String key) {
                Object doc = document.get(key);
                ProcessResult handler = handler(v, doc);
                if (handler.change) {
                   change.set(true);
                    if (isFillChange()) {
                        res.put(key, handler.object);
                    }
                    document.replace(key, handler.document);
                } else {
                    if (isFileUnChange()) {
                        res.put(key, v);
                    }
                }
            } else {
                res.put(k, v);
            }
        });
        return ProcessResult.builder()
                .change(change.get())
                .object(res)
                .document(document)
                .build();
    }

    public boolean isFillChange() {
        return cascadeFillType.equals(CascadeFillType.CASCADED)
                || cascadeFillType.equals(CascadeFillType.ALL);
    }

    public boolean isFileUnChange() {
        return cascadeFillType.equals(CascadeFillType.IGNORED)
                || cascadeFillType.equals(CascadeFillType.ALL);
    }

    @Data
    @Builder
    public static class ProcessResult {
        private Object object;
        private Object document;
        private boolean change;
    }
}
