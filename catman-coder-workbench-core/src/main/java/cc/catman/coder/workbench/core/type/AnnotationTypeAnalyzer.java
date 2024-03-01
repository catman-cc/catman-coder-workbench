package cc.catman.coder.workbench.core.type;

import cc.catman.coder.workbench.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.type.complex.MapType;
import cc.catman.coder.workbench.core.type.complex.StructType;
import cc.catman.coder.workbench.core.type.raw.BooleanRawType;
import cc.catman.coder.workbench.core.type.raw.NumberRawType;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import cc.catman.plugin.classloader.handler.RedirectClassLoaderHandler;
import cc.catman.plugin.classloader.matcher.DefaultClassNameMatcher;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

@Data
@Builder
public class AnnotationTypeAnalyzer {
    /**
     * jdk自带类型过滤
     */
    @Builder.Default
    DefaultClassNameMatcher jdkClassNameMatcher = DefaultClassNameMatcher
            .of(RedirectClassLoaderHandler.JDK_EXCLUDED_PACKAGES);

    /**
     * 白名单
     */
    @Builder.Default
    DefaultClassNameMatcher whiteList = DefaultClassNameMatcher
            .of();

    /**
     * 用于处理引用了自定义类型的场景,此处缓存类型定义和类名称的关系
     */
    @Builder.Default
    Map<Class<?>, DefaultType> typeProcessing = new HashMap<>();


    @Builder.Default
    List<TypeAnalyzer.Analyzer> analyzers = new ArrayList<>(
            Arrays.asList(new TypeAnalyzer.Analyzer() {
                              @Override
                              public boolean support(Class<?> clazz) {
                                  return Object.class.equals(clazz);
                              }

                              @Override
                              public DefaultType analyze(Class<?> clazz) {
                                  return new AnyType();
                              }
                          }
                    , new TypeAnalyzer.Analyzer() {
                        @Override
                        public boolean support(Class<?> clazz) {
                            return AnonymousType.class.equals(clazz);
                        }

                        @Override
                        public DefaultType analyze(Class<?> clazz) {
                            return new AnonymousType();
                        }
                    }
            )
    );

    private Object typeObject;

    private String forceType;

    public DefaultType analyzer() {
        if (typeObject == null) {
            return null;
        }
        if (typeObject instanceof Class<?> clazz) {
            whiteList.addExcludePackage(clazz.getPackage().getName());
            DefaultType analyzer = analyzer(clazz);
            if (StringUtils.hasLength(forceType)) {
                analyzer.typeName = forceType;
            }
            return analyzer;
        }
        whiteList.addExcludePackage(typeObject.getClass().getPackage().getName());
        DefaultType analyzer = analyzer(typeObject);
        if (StringUtils.hasLength(forceType)) {
            analyzer.typeName = forceType;
        }
        return analyzer;
    }

    private DefaultType analyzer(Object obj) {
        Class<?> t = obj.getClass();
        return analyzer(t);
    }

    private DefaultType analyzer(Class<?> t) {
        return analyzer(t, null);
    }

    private ArrayType getArrayType(Class<?> t) {
        return ArrayType.builder()
                .build().setElement(Optional.ofNullable(TypeDescriptor.valueOf(t).getElementTypeDescriptor())
                        .map(epd -> analyzer(epd.getResolvableType().resolve())).orElse(null));
    }

    DefaultType analyzer(Class<?> t, Field field) {
        Optional<TypeAnalyzer.Analyzer> analyzer = analyzers.stream().filter(a -> a.support(t)).findFirst();
        if (analyzer.isPresent()) {
            return analyzer.get().analyze(t);
        }
        if (Map.class.isAssignableFrom(t)) {
            return new MapType();
        } else if (Collection.class.isAssignableFrom(t)) {
            return Optional.ofNullable(field).map(this::getArrayType).orElseGet(() -> getArrayType(t));
        } else if (Number.class.isAssignableFrom(t)) {
            return new NumberRawType();
        } else if (Boolean.class.isAssignableFrom(t)) {
            return new BooleanRawType();
        } else if (String.class.isAssignableFrom(t)) {
            return new StringRawType();
        } else {
            if (jdkClassNameMatcher.match(t.getName())) {
                return null;
            }

            if (!whiteList.match(t.getName())) {
                return null;
            }
            if (typeProcessing.containsKey(t)) {
                return typeProcessing.get(t);
            }


            String name =Optional.ofNullable(AnnotationUtils.getAnnotation(t, TD.class))
                    .map(TD::name)
                    .filter(StringUtils::hasText)
                    .orElse(t.getName());

            // 转换成结构
            StructType st = StructType.builder()
                    .className(name)
                    .build();

            typeProcessing.put(t, st);
            ReflectionUtils.doWithFields(t, f -> {
                Optional.ofNullable(analyzer(f.getType(), f)).ifPresent(ft -> {
                    TD annotation = AnnotationUtils.getAnnotation(f, TD.class);
                    if(Optional.ofNullable(annotation).isEmpty()){
                        st.add(f.getName(), ft);
                        return;
                    }
                    String n=StringUtils.hasText(annotation.name())?annotation.name():f.getName();
                    st.add(TypeDefinition.builder()
                            .name(n)
                            .type(ft)
                                    .required(annotation.required())
                                    .describe(annotation.desc())
                            .build());
                });
            });
            return st;
        }
    }

    private ArrayType getArrayType(Field field) {
        return ArrayType.builder()
                .build().setElement(analyzer(Objects.requireNonNull(TypeDescriptor.nested(field, 1)).getType()));
    }

    public static interface Analyzer {
        boolean support(Class<?> clazz);

        DefaultType analyze(Class<?> clazz);

    }
}
