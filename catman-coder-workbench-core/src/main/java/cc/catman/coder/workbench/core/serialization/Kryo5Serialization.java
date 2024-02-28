package cc.catman.coder.workbench.core.serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;

public class Kryo5Serialization implements ICatManSerialization{
    @Override
    public byte[] serialize(Object obj)  {
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        Output output = new Output(1024, -1);
        kryo.writeClassAndObject(output,obj);
        output.flush();
        output.close();
        return output.getBuffer();
    }

    @Override
    public Object deserialize(byte[] bytes)  {
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        kryo.setCopyReferences(true);
        kryo.setRegistrationRequired(false);
        Input input=new Input(bytes);
        return kryo.readClassAndObject(input);
    }
}
