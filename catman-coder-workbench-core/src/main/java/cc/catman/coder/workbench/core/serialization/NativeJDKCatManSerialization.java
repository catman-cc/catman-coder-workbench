package cc.catman.coder.workbench.core.serialization;


import lombok.SneakyThrows;

import java.io.*;

/**
 * 原生JDK序列化更好的
 * : 支持了对象的序列化,但是其序列化后的字节数组较大,且序列化速度较慢
 */
public class NativeJDKCatManSerialization implements ICatManSerialization {
    @Override
    @SneakyThrows
    public byte[] serialize(Object obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        objectOutput.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    @SneakyThrows
    public Object deserialize(byte[] bytes) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }
}
