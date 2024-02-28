package cc.catman.coder.workbench.core.label;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractLabelSelector<T> implements ILabelSelector<T> {
    @Getter
    protected String match;

    public void setMatch(String match) {
        this.match = match;
        this.matcher = new AntPatchMatcherDecorate(match);
    }

    @Getter
    @Setter

    protected String kind;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Getter
    @Setter

    protected T value;

    @Getter
    @Setter

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    protected List<ILabelSelector<?>> rules = new ArrayList<>();

    @Getter
    @Setter

    @JsonIgnore
    protected JsonNode definition;
    @JsonIgnore
    @Setter
    protected volatile IMatcher matcher;
    @JsonIgnore
    protected static Object UNPACK_FAILED_OBJECT = new Object();
    @JsonIgnore
    protected static Object NULL_OBJECT = new Object();

    public boolean match(String name, ILabelSelectorContext context) {
        return getMatcher().match(name);
    }

    public IMatcher getMatcher() {
        if (matcher == null) {
            synchronized (this) {
                if (matcher == null) {
                    matcher = new AntPatchMatcherDecorate(this.match);
                }
            }
        }
        return this.matcher;
    }

    public boolean valid(Object labels, ILabelSelectorContext context) {
        return findMatchValues(labels).anyMatch(value -> {
            boolean res = doValid(value, context);
            log.debug("[{}] valid result:{},match:{},condition:{},value:{}", getKind(), res, this.getMatch(), this.getValue(), value);
            return res;
        });
    }

    @Override
    public ILabelSelector<T> addRule(ILabelSelector<?> rule) {
        rules.add(rule);
        return this;
    }

    protected boolean doValid(Object object, ILabelSelectorContext context) {
        return false;
    }

    protected boolean assertValueNotNull(Object object) {
        return object != null;
    }

    protected boolean assertValueNotArray(Object object) {
        if (!assertValueNotNull(object)) {
            return false;
        }
        return !object.getClass().isArray() && !(object instanceof Collection);
    }

    protected boolean assertValueNotMap(Object object) {
        if (!assertValueNotNull(object)) {
            return false;
        }
        return !(object instanceof Map);
    }

    protected boolean asserValueIsArray(Object object) {
        if (!assertValueNotNull(object)) {
            return false;
        }
        return object.getClass().isArray();
    }
    protected boolean asserArrayAndThen(Object object, Function<List<Object>, Boolean> then) {
        if (!assertValueNotNull(object)) {
            return false;
        }
        if (object.getClass().isArray()) {
            return then.apply(Arrays.asList((Object[]) object));
        }else if (object instanceof Collection<?> cs){
            return then.apply(new ArrayList<>(cs));
        }
        return false;
    }
    protected boolean unpackAndThen(Object object, Function<Object, Boolean> then) {
        Object unpack = unpackCollection(object);
        if (unpack == UNPACK_FAILED_OBJECT) {
            log.warn("InLabelSelector's value must not be a collection or array,selector:{},value:{}", getDefinition(), object);
            return false;
        }
        if (unpack == NULL_OBJECT) {
            return false;
        }
        return then.apply(unpack);
    }

    protected Object unpackCollection(Object object) {
        if (!assertValueNotNull(object)) {
            return NULL_OBJECT;
        }
        if (object.getClass().isArray()) {
            Object[] objs = (Object[]) object;
            if (objs.length == 1) {
                return objs[0];
            }
            return UNPACK_FAILED_OBJECT;
        }
        if (object instanceof Collection) {
            Collection<?> collection = (Collection<?>) object;
            if (collection.size() == 1) {
                return collection.iterator().next();
            }
            return UNPACK_FAILED_OBJECT;
        }
        return object;
    }

    protected boolean assertIsNumber(Object object) {
        if (!assertValueNotNull(object)) {
            return false;
        }
        return ClassUtils.isAssignable(Number.class, object.getClass());
    }

    protected boolean assertToNumberAndThen(Object object, Function<Number, Boolean> then) {
        if (assertIsNumber(object)) {
            return then.apply((Number) object);
        }
        // 判断是否可以转换为字符串类型
        if (assertValueNotNull(object)) {
            try {
                Double d = Double.valueOf(object.toString());
                return then.apply(d);
            } catch (NumberFormatException e) {
                log.warn("InLabelSelector's value must be a number,selector:{},value:{}", getDefinition().asText(), object);
                return false;
            }
        }
        return false;
    }


    public Stream<Object> findMatchValues(Object object) {
        if (object instanceof Map) {
            return ((Map<String, Object>) object).entrySet().stream()
                    .filter(entry -> getMatcher().match(entry.getKey()))
                    .map(Map.Entry::getValue);
        }
        // 认为是对象
        Class<?> clazz = object.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        return Stream.of(declaredFields).filter(field -> getMatcher().match(field.getName()))
                .peek(ReflectionUtils::makeAccessible)
                .map(field -> ReflectionUtils.getField(field, object));
    }


}
