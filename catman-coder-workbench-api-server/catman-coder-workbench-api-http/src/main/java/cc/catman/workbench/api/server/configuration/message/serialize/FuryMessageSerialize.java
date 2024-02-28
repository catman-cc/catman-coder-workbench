package cc.catman.workbench.api.server.configuration.message.serialize;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerialize;
import org.apache.fury.Fury;
import org.apache.fury.config.FuryBuilder;

public class FuryMessageSerialize implements MessageSerialize {
    public static final byte FURY_SERIALIZE_TYPE = 0x02;

    private final Fury fury;

    public FuryMessageSerialize() {
        this(new FuryBuilder().requireClassRegistration(false)
                .build());
    }

    public FuryMessageSerialize(Fury fury) {
        this.fury = fury;
    }

    @Override
    public Message<?> decode(byte[] bytes) {
        return (Message<?>) fury.deserialize(bytes);
    }

    @Override
    public byte[] encode(Message<?> message) {
        return fury.serialize(message);
    }
}
