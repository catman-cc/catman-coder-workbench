package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.util.ReflectionUtils;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.annotations.Cascade;
import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core.CascadeHelper;
import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core.CascadeType;
import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core.ReadCascadeProcessor;
import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.skip.SkipThreadLocal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Import(CascadeHelper.class)
public class CascadeMongoEventListener extends AbstractMongoEventListener<Object> {
    /**
     * 一个简单的防止递归的操作,但默认情况下对于子线程无法处理,可以考虑使用InheritableThreadLocal,
     * 但是InheritableThreadLocal在使用线程池的背景下需要考虑引入三方库取代此处
     * 这里只做了最简单的处理,可以考虑重写{@link #initThreadLocal()}方法
     */
    public ThreadLocal<List<Object>> IN_PROCESS = initThreadLocal();

    @Resource
    private MongoOperations mongoOperations;

    @Resource
    private CascadeHelper cascadeHelper;

    protected ThreadLocal<List<Object>> initThreadLocal() {
        return ThreadLocal.withInitial(ArrayList::new);
    }

    public @Override void onBeforeSave(BeforeSaveEvent<Object> event) {
        // 在开始保存之前,需要进行筛选,然后对匹配的数据做替换操作,存储-转换,存储的时候需要特殊处理,需要标记一下数据对应的collections
        // 没必要,后面在读取的时候,直接根据原始的数据类型
        if (SkipThreadLocal.skip()) {
            return;
        }

        final Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), cascadeHelper.of(event.getDocument(), event.getSource(), o -> {
            // 这里是被保存的数据,意味着一定被进行了替换操作,所以,相对于的也需要转换该数据,替换document中的相关数据
            try {
                if (IN_PROCESS.get().contains(o)) {
                    // 如果执行到这里,那么一定是出现了循环引用,比如a->b->a这种场景
                    // 理论上此时不应该继续进行保存操作,而是延迟进行处理
                    // 然后退出方法,生成一个代理对象,并返回,
                    return handlerCyclicReference(o);

                }
                IN_PROCESS.get().add(o);
                return mongoOperations.save(o);
            } finally {
                IN_PROCESS.get().remove(o);
            }
        }, CascadeType.SAVE, r -> {
            // try {
            // System.out.println(new JsonMapper().writeValueAsString(r));
            // } catch (JsonProcessingException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
        }));
    }

    @Override
    public void onAfterLoad(AfterLoadEvent<Object> event) {
        if (SkipThreadLocal.skip()) {
            return;
        }
        // 加载时,转换被处理过的数据
        Class<Object> type = event.getType();
        // 递归类型中的所有字段定义,根据字段定义,反向加载document中的内容,然后再处理document中的数据
        Document document = event.getDocument();
        if (Optional.ofNullable(document).isEmpty()) {
            return;
        }

        ReflectionUtils.doWithFields(type, field -> {
            ReflectionUtils.makeAccessible(field);
            Optional.ofNullable(AnnotationUtils.findAnnotation(field, Cascade.class)).ifPresent(cascade -> {
                ReadCascadeProcessor.builder()
                        .cascadeHelper(cascadeHelper)
                        .mongoOperations(mongoOperations)
                        .document(document)
                        .build()
                        .processDocument(field);
            });
        });
    }

    public @Override void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        if (SkipThreadLocal.skip()) {
            return;
        }

        if (event.getDocument() == null || event.getType() == null) {
            return;
        }
        mongoOperations
                .find(new BasicQuery(event.getDocument()), event.getType())
                .forEach(obj -> {
                    Document document = new Document();
                    mongoOperations.getConverter().write(obj, document);
                    ReflectionUtils.doWithFields(
                            obj.getClass(),
                            cascadeHelper.of(document, obj, s -> mongoOperations.remove(s), CascadeType.DELETE, r -> {
                            }));
                });
    }

    protected Object handlerCyclicReference(Object o) {
        log.error("circular references occur during cascading operations:{}", o);
        IN_PROCESS.get().forEach(obj -> {
            log.error("-- {}", obj);
        });
        throw new RuntimeException("");
    }
}