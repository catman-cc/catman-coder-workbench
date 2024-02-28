package cc.catman.coder.workbench.core.message.netty.serialize;

import lombok.Data;
import lombok.Getter;

@Data
public class MessageSerializeConfiguration {
    private byte serializeType=1;

    @Getter
    private MessageSerializeFactory messageSerializeFactory;

    public MessageSerializeConfiguration() {
        messageSerializeFactory = new DefaultMessageSerializeFactory();
//        this.messageSerializeFactory.register(JDKMessageSerialize.JDK_SERIALIZE, new JDKMessageSerialize());
        this.serializeType = JDKMessageSerialize.JDK_SERIALIZE;
    }

    public byte getDefaultSerializeType() {
        return serializeType;
    }
}
