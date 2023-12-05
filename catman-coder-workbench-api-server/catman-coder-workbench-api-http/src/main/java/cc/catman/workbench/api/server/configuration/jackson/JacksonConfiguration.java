package cc.catman.workbench.api.server.configuration.jackson;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.value.DefaultValueProvider;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.SlotType;
import cc.catman.coder.workbench.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.type.complex.MapType;
import cc.catman.coder.workbench.core.type.complex.StructType;
import cc.catman.coder.workbench.core.type.raw.BooleanRawType;
import cc.catman.coder.workbench.core.type.raw.NumberRawType;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import cc.catman.coder.workbench.core.value.Switch.SwitchValueProvider;
import cc.catman.coder.workbench.core.value.ValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderConfig;
import cc.catman.coder.workbench.core.value.expressions.ExpressionsValueProvider;
import cc.catman.coder.workbench.core.value.ifs.IFValueProvider;
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


    @Bean
    public SimpleModule ValueTypeConfigModule(){
        SimpleModule simpleModule=new SimpleModule();
        Class<? extends ValueProvider< ? extends ValueProviderConfig>> dc=DefaultValueProvider.class;
        simpleModule.addDeserializer((Class<ValueProvider<?>>)dc, new UniversalJsonDeserializer<>("kind", (Class<ValueProvider<?>>) dc)
                .add(Constants.ValueProviderKind.IF, IFValueProvider.class)
                .add(Constants.ValueProviderKind.SWITCH, SwitchValueProvider.class)
                .add(Constants.ValueProviderKind.EXPRESSIONS, ExpressionsValueProvider.class)
        );

//        Class<? extends ValueProviderConfig> dcf= DefaultValueProviderConfig.class;
//        simpleModule.addDeserializer((Class<ValueProviderConfig>)dcf, new UniversalJsonDeserializer<>("kind", (Class<ValueProviderConfig>) dcf)
//                .add(Constants.ValueProviderKind.IF, IFValueProviderConfig.class)
//                .add(Constants.ValueProviderKind.SWITCH, SwitchValueProviderConfig.class)
//                .add(Constants.ValueProviderKind.EXPRESSIONS, ExpressionsValueProviderConfig.class)
//        );

        return simpleModule;
    }

}
