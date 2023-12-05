package cc.catman.coder.workbench.core.type;

import cc.catman.plugin.classloader.handler.RedirectClassLoaderHandler;
import cc.catman.plugin.classloader.matcher.DefaultClassNameMatcher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 将类型定义转换为typescript
 */
public class TypeScriptConvert {

    static DefaultClassNameMatcher classNameMatcher = DefaultClassNameMatcher.of(RedirectClassLoaderHandler.JDK_EXCLUDED_PACKAGES);

    /**
     * 用于处理引用了自定义类型的场景,此处缓存类型定义和类名称的关系
     */
    private Map<Type, TSTypeDesc> typeCache = new HashMap<>();


    public List<TSTypeDesc> loadTypes(List<Class<?>> classes) {
//        classes.stream().map(clazz->{
//
//        })
        return null;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        TypeScriptConvert typeScriptConvert = new TypeScriptConvert();
        System.out.println(typeScriptConvert.factory(DemoClass.class));
        System.out.println(typeScriptConvert.toTS());
        System.out.println(123);
    }

    public String toTS() {
        StringBuilder sb = new StringBuilder();
        typeCache.forEach((k, v) -> {
            sb.append(v.toTS());
        });
        return sb.toString();
    }

    public String factory(Type type) {
        if (typeCache.containsKey(type)) {
            return typeCache.get(type).name;
        }
        if (type instanceof Class<?> clazz) {
            if (ClassUtils.isPrimitiveOrWrapper(clazz) || String.class.isAssignableFrom(clazz)) {
                return covertToPrimitive(clazz);
            }
            if (clazz.isArray()) {
                return factory(clazz.getComponentType()) + "[]";
            }
            if (Collection.class.isAssignableFrom(clazz)) {
                ResolvableType componentType = TypeDescriptor.valueOf(clazz).getResolvableType().getComponentType();
                Class<?> resolve = componentType.resolve();
                if (Optional.ofNullable(resolve).isEmpty()) {
                    return "any[]";
                }
                return factory(resolve) + "[]";
            }
            if (classNameMatcher.match(clazz.getCanonicalName())) {
                return "any";
            }
            TSTypeDesc tsTypeDesc = TSTypeDesc.builder()
                    .name(type.getTypeName())
                    .clazz(type)
                    .build();
            typeCache.put(type, tsTypeDesc);
            // 处理枚举类型
            if (clazz.isEnum()) {
                tsTypeDesc.setType("enum");
                Method values = ClassUtils.getStaticMethod(clazz, "values");
                Enum<?>[] es = (Enum<?>[]) ReflectionUtils.invokeMethod(values, null);
                for (Enum<?> e : es != null ? es : new Enum[0]) {
                    // 每一个枚举都应该生成
                    String name = e.name();
//                    tsTypeDesc.add(name, name);
                }
                return tsTypeDesc.name;
            }
            // 处理自定义类型
            tsTypeDesc.setType("class");
            ReflectionUtils.doWithFields(clazz, f -> {
                String name = f.getName();
                TypeDescriptor nested = TypeDescriptor.nested(f, 0);
                if (Optional.ofNullable(nested).isEmpty()) {
                    String t = factory(f.getType());
//                    tsTypeDesc.add(name, t);
                } else {
                    ResolvableType resolvableType = nested.getResolvableType();
//                    tsTypeDesc.add(name, factory(resolvableType.getType()));
                }
            });
            return tsTypeDesc.name;
        }

        if (type instanceof ParameterizedType parameterizedType) {
            TSTypeDesc tsTypeDesc = TSTypeDesc.builder()
                    .name(type.getTypeName())
                    .clazz(type)
                    .build();
            typeCache.put(type, tsTypeDesc);

            // 生成泛型参数定义, Array<T>
            Class<?> rt = (Class<?>)parameterizedType.getRawType();
            if (Collection.class.isAssignableFrom(rt)){
                // 处理array定义
                tsTypeDesc.setType("array");
                tsTypeDesc.setName("Array");


            }else if (Map.class.isAssignableFrom(rt)){
                tsTypeDesc.setType("map");
            }
            tsTypeDesc.setType("class");
            for (Type actualTypeArgument : parameterizedType.getActualTypeArguments()) {
                String at = factory(actualTypeArgument);
                tsTypeDesc.addParameterized(at);
            }
            return tsTypeDesc.name;
            // 不是集合类型,那就需要处理了
            }
        return "any";
    }

    private String covertToPrimitive(Class<?> clazz) {
        clazz = ClassUtils.resolvePrimitiveIfNecessary(clazz);
        if (String.class.isAssignableFrom(clazz)) {
            return "string";
        }

        if (Number.class.isAssignableFrom(clazz)) {
            return "number";
        }

        if (Boolean.class.isAssignableFrom(clazz)) {
            return "boolean";
        }

        if (Map.class.isAssignableFrom(clazz)) {
            return "object";
        }
        return "any";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TSTypeDesc {
        public static List<String> GENERICS_NAMES = Arrays.asList(
                "T",
                "R",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"
        );
        public static List<String> INNER_TYPE=Arrays.asList("string","boolean","number");
        private String name;
        private String type;
        private Type clazz;
        @Builder.Default
        private List<String> parameterizedType = new ArrayList<>();

        private String content;
        @Builder.Default
        private LinkedHashMap<String, TSTypeDesc> fields = new LinkedHashMap<>();

        public void add(String name, TSTypeDesc type) {
            fields.put(name, type);
        }

        public void addParameterized(String type) {
            if (parameterizedType.contains(type)) {
                return;
            }
            parameterizedType.add(type);
        }

        public String toTS() {
            StringBuilder ts = new StringBuilder(type);
            String[] split = name.replaceAll("[\\>|\\[|\\]]","").split("[\\.|\\$]");
            ts.append(" ").append(split[split.length-1]);
            if (parameterizedType.size()>0){
                ts.append("<");
                ts.append(
                        IntStream
                                .range(0,parameterizedType.size())
                                .mapToObj(i-> {
                                    String pt = parameterizedType.get(i);
                                    if (INNER_TYPE.contains(pt)){
                                        return pt;
                                    }
                                    return GENERICS_NAMES.get(i) + " extends " +pt;
                                })
                                .collect(Collectors.joining(","))
                );
                ts.append(">");
            }

            ts.append("{").append("\r");
            fields.forEach((k, v) -> {
                ts.append("\t").append(k).append("!:");
                // 处理泛型定义
                if (parameterizedType.contains(v)) {
                    ts.append(GENERICS_NAMES.get(parameterizedType.indexOf(v)));
                } else {
                    ts.append(v);
                }
                ts.append(";\r");
            });
            ts.append("}\r");
            return ts.toString();
        }
    }

    public static class DemoClass {
        private boolean oriBoolVar;
        private int oriIntVar;
        private long oriLongVar;

        private Boolean boolVar;
        private Integer intVar;
        private Long longVar;

        private String strVar;
        private Date dateVar;

        private String[] arrayStrVar;
        private String[][] array2StrVar;
        private String[][][] array3StrVar;

        private List<String> listStrVar;
        private List<Integer> listIntVar;
        private List<Long> listLongVar;

        private Map<String, String> mapStrVar;
        private Map<Integer, String> mapIntVar;
        private Map<Long, Boolean> mapLongVar;

        private List<List<String>> listListStrVar;

        private List<DemoClass2> listDemoClass;
        private List<Map<String, String>> listMapStringString;
    }

    public static class DemoClass2 {
        private boolean oriBoolVar;
        private int oriIntVar;
        private long oriLongVar;
    }
}
