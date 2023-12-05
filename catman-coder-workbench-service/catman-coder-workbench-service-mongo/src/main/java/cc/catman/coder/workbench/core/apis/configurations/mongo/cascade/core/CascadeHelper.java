package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.MongoCollectionUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascade;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CascadeHelper {
    final private ConversionService conversionService;

    @Getter
    final private MongoOperations mongoOperations;

    @Getter
    final private ExpressionParser expressionParser = createExpressionParser();

    public CascadeHelper(@Autowired MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
        this.conversionService = mongoOperations.getConverter().getConversionService();

    }

    protected ExpressionParser createExpressionParser() {
        return new SpelExpressionParser();
    }

    public ReflectionUtils.FieldCallback of(Document document, Object obj, Function<Object, Object> func,
            CascadeType supportType, Consumer<WriteCascadeProcessor.ProcessResult> consumer) {
        return field -> {
            ReflectionUtils.makeAccessible(field);

            Cascade cascade = AnnotationUtils.getAnnotation(field, Cascade.class);
            if (Optional.ofNullable(cascade).isEmpty()) {
                return;
            }

            final Stream<CascadeType> ss = Stream.of(supportType, CascadeType.ALL);
            if (ss.noneMatch(s -> s.equals(cascade.value()))) {
                return;
            }

            Object fieldValue = ReflectionUtils.getField(field, obj);
            if (Optional.ofNullable(fieldValue).isEmpty()) {
                return;
            }

            EvaluationContext context = new StandardEvaluationContext();
            ContextVariable variables = ContextVariable.builder()
                    .root(obj)
                    .field(field)
                    .type(field.getType())
                    .value(fieldValue)
                    .cascade(cascade)
                    .supportType(supportType)
                    .build();

            variables.toMap().forEach(context::setVariable);

            // 当前字段在document中对应的key值
            String key = findFieldName(field);

            Object doc = document.get(key);
            WriteCascadeProcessor writeCascadeProcessor = new WriteCascadeProcessor(this, expressionParser, context,
                    mongoOperations, cascade, func, supportType);
            WriteCascadeProcessor.ProcessResult handler = writeCascadeProcessor.handler(fieldValue, doc);
            if (handler.isChange()) {
                document.replace(key, handler.getDocument());
            }
            consumer.accept(handler);
            Object v = handler.getObject();
            if (log.isDebugEnabled()) {
                log.debug("because the @Cascade annotation is marked, the cascade operation will be carried out. {}",
                        v);
            }
            if (Optional.ofNullable(v).isEmpty()) {
                return;
            }

            TypeDescriptor vtd = TypeDescriptor.forObject(v);
            TypeDescriptor vdd = TypeDescriptor.valueOf(field.getType());

            if (conversionService.canConvert(vtd, Objects.requireNonNull(vdd))) {
                ReflectionUtils.setField(field, obj, conversionService.convert(v, vdd.getObjectType()));
            }
        };
    }

    public String findFieldName(Field field) {
        String key = field.getName();
        org.springframework.data.mongodb.core.mapping.Field fieldAnnotation = AnnotationUtils.findAnnotation(field,
                org.springframework.data.mongodb.core.mapping.Field.class);
        if (Optional.ofNullable(fieldAnnotation).isPresent()) {
            if (StringUtils.hasText(fieldAnnotation.name())) {
                key = fieldAnnotation.name();
            }
        }
        return key;
    }

    public static String findCollectionsName(Class<?> type) {
        return Optional
                .ofNullable(AnnotationUtils.findAnnotation(type,
                        org.springframework.data.mongodb.core.mapping.Document.class))
                .map((document -> {
                    String collection = document.collection();
                    if (StringUtils.hasText(collection)){
                        return  collection;
                    }
                    return  MongoCollectionUtils.getPreferredCollectionName(type);
                }))
                .orElseGet(() -> MongoCollectionUtils.getPreferredCollectionName(type)
                );
    }

    public static String findIdValue(Object obj) {
        return Optional.ofNullable(obj).map(o -> {
            AtomicReference<Field> field = new AtomicReference<>();
            ReflectionUtils.doWithFields(o.getClass(), f -> {
                if (Optional.ofNullable(field.get()).isPresent()) {
                    return;
                }
                if (Optional.ofNullable(AnnotationUtils.findAnnotation(f, MongoId.class)).isPresent()
                        || Optional.ofNullable(AnnotationUtils.findAnnotation(f, Id.class)).isPresent()) {
                    field.set(f);
                }
            });
            return Optional.ofNullable(field.get())
                    .map(f -> {
                        ReflectionUtils.makeAccessible(f);
                        return Optional.ofNullable(ReflectionUtils.getField(f, o))
                                .map(String::valueOf).orElse("");
                    }).orElse("");

        }).orElse("");
    }

    public static String findIdName(Class<?> clazz) {
        AtomicReference<String> name = new AtomicReference<>("");
        ReflectionUtils.doWithFields(clazz, f -> {
            if (StringUtils.hasText(name.get())) {
                return;
            }
            ReflectionUtils.makeAccessible(f);
            if (Optional.ofNullable(AnnotationUtils.findAnnotation(f, MongoId.class)).isEmpty()
                    && Optional.ofNullable(AnnotationUtils.findAnnotation(f, Id.class)).isEmpty()) {
                return;
            }
            String idName = Optional.ofNullable(
                    AnnotationUtils.findAnnotation(
                            f, org.springframework.data.mongodb.core.mapping.Field.class))
                    .map(org.springframework.data.mongodb.core.mapping.Field::name)
                    .orElseGet(f::getName);
            name.set(idName);
        });
        return name.get();
    }
}
