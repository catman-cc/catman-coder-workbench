package cc.catman.coder.workbench.core.serialization;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FstKSonSerializerTest {

    @Test
    void serialize() {
        FstKSonSerializer fstKSonSerializer = new FstKSonSerializer();
        byte[] serialize = fstKSonSerializer.serialize(circle());
        System.out.println(serialize);
    }
    Object circle(){
        Map<String,Object> a=new HashMap<>();
        Map<String,Object> b=new HashMap<>();
        a.put("b",b);
        b.put("a",a);
        return a;
    }
}