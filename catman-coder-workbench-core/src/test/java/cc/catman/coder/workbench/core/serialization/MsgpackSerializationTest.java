package cc.catman.coder.workbench.core.serialization;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


class MsgpackSerializationTest {

    @Test
    void serialize() {
        MsgpackSerialization serialization=MsgpackSerialization.create();
        serialization.serialize(circle());
    }

    @Test
    void deserialize() {


    }

    Object circle(){
        Map<String,Object> a=new HashMap<>();
        Map<String,Object> b=new HashMap<>();
        a.put("b",b);
        b.put("a",a);
        return a;
    }
}