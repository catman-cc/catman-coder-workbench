package cc.catman.coder.workbench.core.message;

public interface IMessageDecoderFactory {

    IMessageDecoder find(Message<?> message);
    IMessageDecoderFactory add(IMessageDecoder messageDecoder);

    IMessageDecoderFactory setDefaultMessageDecoder(IMessageDecoder defaultMessageDecoder);

    <T> Message<T> decode(Message<?> message,Class<T> clazz);
}
