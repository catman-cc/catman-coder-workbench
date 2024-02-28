package cc.catman.coder.workbench.core.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.type.*;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class MsgpackSerialization implements ICatManSerialization {
    private  final ObjectMapper objectMapper;
    public MsgpackSerialization(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static MsgpackSerialization create(){
        return new MsgpackSerialization(createMsgpackObjectMapper());
    }
    public static ObjectMapper createMsgpackObjectMapper(){
//        MessagePackFactory messagePackFactory = new MessagePackFactory();
//        messagePackFactory.setReuseResourceInParser(true);
//        messagePackFactory.setReuseResourceInGenerator(true);
        ObjectMapper om = new ObjectMapper();
        om.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        Map<String,Integer> cache=new HashMap<>();
        om.registerModule(new SimpleModule(){
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new BeanSerializerModifier() {
                    @Override
                    public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return createProxySerializer(super.modifySerializer(config, beanDesc, serializer), cache);
                    }

                    @Override
                    public JsonSerializer<?> modifyArraySerializer(SerializationConfig config, ArrayType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return createProxySerializer(super.modifyArraySerializer(config, valueType, beanDesc, serializer), cache);
                    }

                    @Override
                    public JsonSerializer<?> modifyCollectionSerializer(SerializationConfig config, CollectionType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return createProxySerializer(super.modifyCollectionSerializer(config, valueType, beanDesc, serializer), cache);
                    }

                    @Override
                    public JsonSerializer<?> modifyCollectionLikeSerializer(SerializationConfig config, CollectionLikeType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return createProxySerializer(super.modifyCollectionLikeSerializer(config, valueType, beanDesc, serializer), cache);
                    }

                    @Override
                    public JsonSerializer<?> modifyMapSerializer(SerializationConfig config, MapType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        MapSerializer mapSerializer = (MapSerializer) super.modifyMapSerializer(config, valueType, beanDesc, serializer);

//                        return new CachedMapSerializer(mapSerializer,null,true,cache);
                        return createProxySerializer(super.modifyMapSerializer(config, valueType, beanDesc, serializer), cache);
//                        return createProxySerializer(super.modifyMapSerializer(config, valueType, beanDesc, serializer), cache);
                    }

                    @Override
                    public JsonSerializer<?> modifyMapLikeSerializer(SerializationConfig config, MapLikeType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return createProxySerializer(super.modifyMapLikeSerializer(config, valueType, beanDesc, serializer), cache);
                    }

                    @Override
                    public JsonSerializer<?> modifyEnumSerializer(SerializationConfig config, JavaType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return createProxySerializer(super.modifyEnumSerializer(config, valueType, beanDesc, serializer), cache);
                    }

                    @Override
                    public JsonSerializer<?> modifyKeySerializer(SerializationConfig config, JavaType valueType, BeanDescription beanDesc, JsonSerializer<?> serializer) {
                        return super.modifyKeySerializer(config, valueType, beanDesc, serializer);
                    }

                    public JsonSerializer createProxy(JsonSerializer raw, Map<String, Integer> cache) {
                        // 创建代理,拦截serialize方法
                        Class<? extends JsonSerializer> rawClass = raw.getClass();
                        if (raw instanceof MapSerializer ms){
                            Field filterId = ReflectionUtils.findField(MapSerializer.class, "_filterId");
                            ReflectionUtils.makeAccessible(filterId);
                            Field sortKeys=ReflectionUtils.findField(MapSerializer.class,"_sortKeys");
                            ReflectionUtils.makeAccessible(sortKeys);
                            String fi= (String) ReflectionUtils.getField(filterId,ms);
                            Boolean sk= (Boolean) ReflectionUtils.getField(sortKeys,ms);
                            return (MapSerializer) org.springframework.cglib.proxy.Proxy.newProxyInstance(MapSerializer.class.getClassLoader(), new Class[]{ContainerSerializer.class}, new JsonSerializerInvocationHandler(cache, raw));

                        }
                        return raw;
                    }
                    public JsonSerializer createProxySerializer(JsonSerializer raw, Map<String, Integer> cache) {
                        // 创建代理,拦截serialize方法
                        Class<? extends JsonSerializer> rawClass = raw.getClass();
                        Enhancer enhancer=new Enhancer();
                        enhancer.setSuperclass(rawClass);
                        enhancer.setCallback(new JsonSerializerMethodInterceptor(cache,raw));
//                        enhancer.setInterceptDuringConstruction(false);
//return (JsonSerializer) enhancer.create();
                        if (raw instanceof MapSerializer ms){
                            Field filterId = ReflectionUtils.findField(MapSerializer.class, "_filterId");
                            ReflectionUtils.makeAccessible(filterId);
                            Field sortKeys=ReflectionUtils.findField(MapSerializer.class,"_sortKeys");
                            ReflectionUtils.makeAccessible(sortKeys);
                            String fi= (String) ReflectionUtils.getField(filterId,ms);
                            Boolean sk= (Boolean) ReflectionUtils.getField(sortKeys,ms);
                            return (MapSerializer)enhancer.create(new Class[]{MapSerializer.class, Object.class,boolean.class},new Object[]{ms,fi,sk});
                        }

                        Constructor<?>[] declaredConstructors = rawClass.getDeclaredConstructors();
//                         然后寻找一个可用的构造参数,筛选条件
                        for (Constructor<?> declaredConstructor : declaredConstructors) {
                            Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
                            declaredConstructor.setAccessible(true);
                            Object[] objects = new Object[parameterTypes.length];
                            for (int i = 0; i < objects.length; i++) {
                                objects[i]=null;//parameterTypes[i
                            }
                            try {
                                return (JsonSerializer) enhancer.create(parameterTypes, objects);
                            } catch (Exception e) {
                                // ignore
                                System.out.println(e);
                            }
                        }
                        throw new RuntimeException("未找到合适的构造方法");
//                        return new JsonSerializer() {
//                            @Override
//                            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                                // 排除基本类型
//                                if (ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
//                                    raw.serialize(value, gen, serializers);
//                                } else {
//                                    Integer id = cache.get(value.getClass().getName());
//                                    if (id == null) {
//                                        id = System.identityHashCode(value);
//                                        cache.put(value.getClass().getName(), id);
//                                        raw.serialize(value, gen, serializers);
//                                    } else {
//                                        gen.writeStartObject();
//                                        gen.writeFieldName("@id");
//                                        gen.writeNumber(id);
//                                        gen.writeEndObject();
//                                    }
//                                }
//                            }
//                        };
                    }
                });
            }
        });
        return om;
    }

    @Override
    @SneakyThrows
    public byte[] serialize(Object obj) {
        return objectMapper.writeValueAsBytes(obj);
    }

    @Override
    @SneakyThrows
    public Object deserialize(byte[] bytes) {
        return objectMapper.readValue(bytes, Object.class);
    }

    public static class JsonSerializerInvocationHandler implements InvocationHandler {
        private  Map<String,Integer> cache;

        private JsonSerializer raw;

        public JsonSerializerInvocationHandler(Map<String, Integer> cache, JsonSerializer proxy) {
            this.cache = cache;
            this.raw = proxy;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("serialize")&&args.length==3) {
                Object value = args[0];
                JsonGenerator gen = (JsonGenerator) args[1];
//                SerializerProvider serializers= (SerializerProvider) args[2];
                // 此处执行代理逻辑
                if (ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
                    return method.invoke(raw, args);
                } else {
                    Integer id = cache.get(value.getClass().getName());
                    if (id == null) {
                        id = System.identityHashCode(value);
                        cache.put(value.getClass().getName(), id);
                        return method.invoke(raw, args);
                    } else {
                        gen.writeStartObject();
                        gen.writeFieldName("@id");
                        gen.writeNumber(id);
                        gen.writeEndObject();
                        return null;
                    }
                }
            }
            return method.invoke(raw,args);
        }
    }


    public static  class JsonSerializerMethodInterceptor implements MethodInterceptor{
        private  Map<String,Integer> cache;

        private JsonSerializer raw;

        public JsonSerializerMethodInterceptor(Map<String, Integer> cache, JsonSerializer proxy) {
            this.cache = cache;
            this.raw = proxy;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            if (method.getName().equals("serialize")&&args.length==3){
                Object value= args[0];
                JsonGenerator gen= (JsonGenerator) args[1];
                SerializerProvider serializers= (SerializerProvider) args[2];
                // 此处执行代理逻辑
                if (ClassUtils.isPrimitiveOrWrapper(value.getClass())) {
                    return proxy.invokeSuper(obj,args);
                } else {
                    Integer id = cache.get(value.getClass().getName());
                    if (id == null) {
                        id = System.identityHashCode(value);
                        cache.put(value.getClass().getName(), id);
                        return proxy.invokeSuper(obj,args);
                    } else {
                        gen.writeStartObject();
                        gen.writeFieldName("@id");
                        gen.writeNumber(id);
                        gen.writeEndObject();
                        return null;
                    }
                }
            }else if (method.getName().equals("withResolved")) {
                return null;
            }
            return proxy.invokeSuper(obj,args);
        }
    }
   }

