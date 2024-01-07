package cc.catman.workbench.api.server.configuration.jackson;

import cc.catman.coder.workbench.core.Constants;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.message.CommandMessage;
import cc.catman.coder.workbench.core.message.message.MessagePayloadDeserializer;
import cc.catman.coder.workbench.core.message.message.TextMessage;
import cc.catman.coder.workbench.core.type.AnonymousType;
import cc.catman.coder.workbench.core.type.DefaultType;
import cc.catman.coder.workbench.core.type.GenericType;
import cc.catman.coder.workbench.core.type.SlotType;
import cc.catman.coder.workbench.core.type.complex.ArrayType;
import cc.catman.coder.workbench.core.type.complex.MapType;
import cc.catman.coder.workbench.core.type.complex.ReferType;
import cc.catman.coder.workbench.core.type.complex.StructType;
import cc.catman.coder.workbench.core.type.raw.BooleanRawType;
import cc.catman.coder.workbench.core.type.raw.NumberRawType;
import cc.catman.coder.workbench.core.type.raw.StringRawType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class JacksonConfiguration {

    @Bean
    public SimpleModule TypeDeserializerModule(){
        // 这里使用mapper.registerSubtypes()注册子类,在反序列化时,会根据json中的type字段,自动选择对应的子类进行反序列化
        // 比这种方法要简单很多, simpleModule.registerSubtypes()
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
                        .add(Constants.Type.TYPE_NAME_ANY, DefaultType.class)
                        .add(Constants.Type.TYPE_NAME_REFER, ReferType.class)
                        .add(Constants.Type.TYPE_NAME_ANONYMOUS, AnonymousType.class)
                        .add(Constants.Type.TYPE_NAME_GENERIC, GenericType.class)

        );
        simpleModule.addDeserializer(Message.class,new MessagePayloadDeserializer());
        simpleModule.registerSubtypes(CommandMessage.class, TextMessage.class);
        return simpleModule;
    }


//    @Bean
//    public SimpleModule ValueTypeConfigModule(){
//        SimpleModule simpleModule=new SimpleModule();
//        Class<? extends ValueProvider dc=DefaultValueProvider.class;
//        simpleModule.addDeserializer((Class<ValueProvider<?>>)dc, new UniversalJsonDeserializer<>("kind", (Class<ValueProvider<?>>) dc)
//                .add(Constants.ValueProviderKind.IF, IFValueProvider.class)
//                .add(Constants.ValueProviderKind.SWITCH, SwitchValueProvider.class)
//                .add(Constants.ValueProviderKind.EXPRESSIONS, ExpressionsValueProvider.class)
//        );
//
////        Class<? extends ValueProviderConfig> dcf= DefaultValueProviderConfig.class;
////        simpleModule.addDeserializer((Class<ValueProviderConfig>)dcf, new UniversalJsonDeserializer<>("kind", (Class<ValueProviderConfig>) dcf)
////                .add(Constants.ValueProviderKind.IF, IFValueProviderConfig.class)
////                .add(Constants.ValueProviderKind.SWITCH, SwitchValueProviderConfig.class)
////                .add(Constants.ValueProviderKind.EXPRESSIONS, ExpressionsValueProviderConfig.class)
////        );
//
//        return simpleModule;
//    }

}
