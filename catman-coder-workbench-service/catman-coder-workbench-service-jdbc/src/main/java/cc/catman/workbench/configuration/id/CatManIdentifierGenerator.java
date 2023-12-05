package cc.catman.workbench.configuration.id;

import cc.catman.coder.workbench.core.Constants;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Id;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class CatManIdentifierGenerator implements IdentifierGenerator {
    private WeakHashMap<Class<?>, Field> cache = new WeakHashMap<>();

    protected IdGenerator idGenerator;

    public CatManIdentifierGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public CatManIdentifierGenerator() {
        this(new AlternativeJdkIdGenerator());
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        Class<?> aClass = object.getClass();
        Field idField = Optional.ofNullable(cache.get(aClass)).orElseGet(() -> {
            // 解析对象.查找id字段
            AtomicReference<Field> field = new AtomicReference<>(cache.get(aClass));
            ReflectionUtils.doWithFields(aClass, field::set, f -> Optional.ofNullable(AnnotationUtils.findAnnotation(f, Id.class)).isPresent());
            Field f = field.get();
            cache.put(aClass, f);
            return f;
        });

        if (Optional.ofNullable(idField).isPresent()) {
            idField.setAccessible(true);
            try {
                // 优先使用现有ID,但是需要排除掉临时ID
                return useOrCreateID(idField.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this.idGenerator.generateId();
    }
    protected String useOrCreateID(Object id){
        return Optional.ofNullable(id)
                .map(oid->{
                    String nid=String.valueOf(oid);
                    if (nid.startsWith(Constants.TEMP_ID_SUFFIX)){
                        return this.idGenerator.generateId().toString();
                    }
                    return nid;
                }).orElseGet(()-> this.idGenerator.generateId().toString());
    }
}
