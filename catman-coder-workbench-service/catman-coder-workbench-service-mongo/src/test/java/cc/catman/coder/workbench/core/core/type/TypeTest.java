package cc.catman.coder.workbench.core.core.type;

import cc.catman.coder.workbench.core.core.type.raw.StringRawType;
import cc.catman.coder.workbench.core.core.type.complex.StructType;
import org.junit.Test;

public class TypeTest {

    @Test
    public void testForBuild(){
        Type type= StructType.builder()
                .build()
                .add(TypeDefinition.builder()
                        .name("name")
                        .type(new StringRawType())
                        .build()
                );
    }

}