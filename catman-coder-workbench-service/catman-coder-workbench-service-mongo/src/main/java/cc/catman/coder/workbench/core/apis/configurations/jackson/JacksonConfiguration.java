package cc.catman.coder.workbench.core.apis.configurations.jackson;

import cc.catman.coder.workbench.core.core.Constants;
import cc.catman.coder.workbench.core.core.type.DefaultType;
import cc.catman.coder.workbench.core.core.type.SlotType;
import cc.catman.coder.workbench.core.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.core.type.complex.MapType;
import cc.catman.coder.workbench.core.core.type.complex.StructType;
import cc.catman.coder.workbench.core.core.type.raw.BooleanRawType;
import cc.catman.coder.workbench.core.core.type.raw.NumberRawType;
import cc.catman.coder.workbench.core.core.type.raw.StringRawType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JacksonConfiguration {

    @Bean
    public SimpleModule TypeDeserializerModule(){
        SimpleModule simpleModule=new SimpleModule();
        simpleModule.addDeserializer(DefaultType.class
                ,new TypeDeserializer()
                        .add(Constants.Type.TYPE_NAME_STRING, StringRawType.class)
                        .add(Constants.Type.TYPE_NAME_NUMBER, NumberRawType.class)
                        .add(Constants.Type.TYPE_NAME_STRUCT, StructType.class)
                        .add(Constants.Type.TYPE_NAME_ARRAY, ArrayType.class)
                        .add(Constants.Type.TYPE_NAME_BOOLEAN, BooleanRawType.class)
                        .add(Constants.Type.TYPE_NAME_MAP, MapType.class)
                        .add(Constants.Type.TYPE_NAME_SLOT, SlotType.class)

        );
        return simpleModule;
    }


}
