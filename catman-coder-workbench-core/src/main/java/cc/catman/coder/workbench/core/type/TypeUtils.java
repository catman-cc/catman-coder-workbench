package cc.catman.coder.workbench.core.type;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import cc.catman.coder.workbench.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.type.complex.MapType;
import cc.catman.coder.workbench.core.type.complex.StructType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import cc.catman.coder.workbench.core.type.raw.BooleanRawType;
import cc.catman.coder.workbench.core.type.raw.NumberRawType;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import cc.catman.plugin.classloader.handler.RedirectClassLoaderHandler;
import cc.catman.plugin.classloader.matcher.DefaultClassNameMatcher;

/**
 * 请一定注意,尽可能不要出现循环引用
 */
public class TypeUtils {

    static DefaultClassNameMatcher classNameMatcher = DefaultClassNameMatcher
            .of(RedirectClassLoaderHandler.JDK_EXCLUDED_PACKAGES);
    // static Map<Class<?>, DefaultType> typeCached = new HashMap<>();

    static Map<Class<?>, DefaultType> typeProcessing = new HashMap<>();

    public static DefaultType of(Object obj) {
        Class<?> t = obj.getClass();
        return of(t);
    }

    public static DefaultType of(Class<?> t) {
        if (t == null) {
            return null;
        }
        // if (typeCached.containsKey(t)) {
        // return typeCached.get(t);
        // }

        if (Map.class.isAssignableFrom(t)) {
            return new MapType();
        } else if (Collection.class.isAssignableFrom(t)) {
            return ArrayType.builder()
                    .build().setElement(Optional.ofNullable(TypeDescriptor.valueOf(t).getElementTypeDescriptor())
                            .map(epd -> of(epd.getResolvableType().resolve())).orElse(null));
        } else if (Number.class.isAssignableFrom(t)) {
            return new NumberRawType();
        } else if (Boolean.class.isAssignableFrom(t)) {
            return new BooleanRawType();
        } else if (String.class.isAssignableFrom(t)) {
            return new StringRawType();
        } else {
            if (classNameMatcher.match(t.getCanonicalName())) {
                return null;
            }
            IsType isType = AnnotationUtils.getAnnotation(t, IsType.class);
            if (isType == null) {
                return null;
            }
            // if (typeCached.containsKey(t)) {
            // return typeCached.get(t);
            // }
            if (typeProcessing.containsKey(t)) {
                return typeProcessing.get(t);
            }

            String name = isType.value();
            if (!StringUtils.hasText(name)) {
                name = t.getCanonicalName();
            }
            // 转换成结构
            StructType st = StructType.builder()
                    .className(name)
                    .build();
            typeProcessing.put(t, st);
            ReflectionUtils.doWithFields(t, f -> {
                Optional.ofNullable(of(f)).ifPresent(ft -> {
                    st.add(f.getName(), ft);
                });

            });
            typeProcessing.remove(t, st);
            // typeCached.put(t, st);
            return st;
        }

    }

    static DefaultType of(Field field) {
        Class<?> t = field.getType();

        // if (typeCached.containsKey(t)) {
        // return typeCached.get(t);
        // }

        if (Map.class.isAssignableFrom(t)) {
            return new MapType();
        } else if (Collection.class.isAssignableFrom(t)) {
            return ArrayType.builder()
                    .build().setElement(of(Objects.requireNonNull(TypeDescriptor.nested(field, 1)).getType()));
        } else if (Number.class.isAssignableFrom(t)) {
            return new NumberRawType();
        } else if (Boolean.class.isAssignableFrom(t)) {
            return new BooleanRawType();
        } else if (String.class.isAssignableFrom(t)) {
            return new StringRawType();
        } else {
            if (classNameMatcher.match(t.getCanonicalName())) {
                return null;
            }
            IsType isType = AnnotationUtils.getAnnotation(t, IsType.class);
            if (isType == null) {
                return null;
            }
            // if (typeCached.containsKey(t)) {
            // return typeCached.get(t);
            // }
            if (typeProcessing.containsKey(t)) {
                return typeProcessing.get(t);
            }

            String name = isType.value();
            if (!StringUtils.hasText(name)) {
                name = t.getCanonicalName();
            }
            // 转换成结构
            StructType st = StructType.builder()
                    .className(name)
                    .build();
            typeProcessing.put(t, st);
            ReflectionUtils.doWithFields(t, f -> {
                Optional.ofNullable(of(f)).ifPresent(ft -> {
                    st.add(f.getName(), ft);
                });
            });
            typeProcessing.remove(t, st);
            // typeCached.put(t, st);
            return st;
        }
    }
}
