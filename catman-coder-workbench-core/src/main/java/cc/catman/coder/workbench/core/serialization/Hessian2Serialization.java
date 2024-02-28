package cc.catman.coder.workbench.core.serialization;

import com.caucho.hessian.io.*;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Hessian2序列化,性能比jdk序列化好
 */
public class Hessian2Serialization implements ICatManSerialization{
    @Override
    @SneakyThrows
    public byte[] serialize(Object obj)  {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        HessianOutput hessianOutput = new HessianOutput(byteArrayOutputStream);
        hessianOutput.writeObject(obj);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    @SneakyThrows
    public Object deserialize(byte[] bytes)  {
        ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(bytes);
        HessianInput hessianInput = new HessianInput(byteArrayInputStream);
        return hessianInput.readObject();
    }
}
