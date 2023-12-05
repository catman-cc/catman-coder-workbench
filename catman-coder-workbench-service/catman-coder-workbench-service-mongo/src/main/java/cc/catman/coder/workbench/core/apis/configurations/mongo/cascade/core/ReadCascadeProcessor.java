package cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ReadCascadeProcessor implements ICascadeProcessor {
    private Document document;
    private MongoOperations mongoOperations;
    final private CascadeHelper cascadeHelper;

    protected Document processDocument(Field field) {
        final String key = cascadeHelper.findFieldName(field);
        Object res = processDocument(document.get(key));
        document.replace(key,res );
        return document;
    }

    protected Collection<?> processDocument(Collection<?> list) {
        return list.stream()
                .map(this::processDocument)
                .toList();
    }

    protected Document processDocument(Document doc) {
        return CascadeInfo.from(doc)
                .filter(CascadeInfo::isValid)
                .map(info -> mongoOperations.findById(info.getId(), Document.class, info.getCollection()))
                .orElseGet(() -> {
                    // 递归处理
                    new HashMap<>(doc).forEach((k, v) -> {
                        Object o = processDocument(v);
                        if (!v.equals(o)) {
                            doc.replace(k, o);
                        }
                    });
                    return doc;
                });
    }

    public Object processDocument(Object obj) {

        if (obj instanceof Collection<?> cs) {
            // 集合类型的
            return processDocument(cs);
        } else if (obj instanceof Document doc) {
            return processDocument(doc);
        } else if (obj instanceof Field) {
            processDocument((Field) obj);
        }
        return obj;
    }

}
