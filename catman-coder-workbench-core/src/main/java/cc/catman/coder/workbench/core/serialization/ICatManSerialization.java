package cc.catman.coder.workbench.core.serialization;

public interface ICatManSerialization {
/**
     * 序列化
     * @param obj 需要序列化的对象
     * @return 序列化后的字节数组
     */
    byte[] serialize(Object obj)  ;

    /**
     * 反序列化
     * @param bytes 需要反序列化的字节数组
     * @return 反序列化后的对象
     */
    Object deserialize(byte[] bytes)  ;


    /**
     * 序列化
     * @param obj 需要序列化的对象
     * @return 序列化后的字符串
     */
   default String serializeToString(Object obj)  {
        return new String(serialize(obj));
    }

    /**
     * 反序列化
     * @param bytes 需要反序列化的字节数组
     * @param clazz 反序列化后的对象类型
     * @param <T> 反序列化后的对象类型
     * @return 反序列化后的对象
     */
   default  <T> T deserialize(byte[] bytes, Class<T> clazz)  {
        return (T)deserialize(bytes);
   }

    /**
     * 反序列化
     * @param str 需要反序列化的字符串
     * @param clazz 反序列化后的对象类型
     * @param <T> 反序列化后的对象类型
     * @return 反序列化后的对象
     */
   default  <T> T deserialize(String str, Class<T> clazz)  {
        return deserialize(str.getBytes(), clazz);
   }



    /**
     * 反序列化
     * @param str 需要反序列化的字符串
     * @return 反序列化后的对象
     */
   default Object deserialize(String str)  {
        return deserialize(str.getBytes());
   }
}
