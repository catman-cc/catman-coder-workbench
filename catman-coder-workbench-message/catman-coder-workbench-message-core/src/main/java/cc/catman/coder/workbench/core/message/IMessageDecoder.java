package cc.catman.coder.workbench.core.message;

public interface IMessageDecoder {
    boolean support(Message<?> message);

    <T> Message<T> decode(Message<?> message,Class<T> clazz);
}
