package cc.catman.coder.workbench.core.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DefaultMessageDecoderFactory implements IMessageDecoderFactory{

    private List<IMessageDecoder> messageDecoders=new ArrayList<>();

    private IMessageDecoder defaultMessageDecoder=new IMessageDecoder() {
        @Override
        public boolean support(Message<?> message) {
            return true;
        }

        @Override
        public <T> Message<T> decode(Message<?> message, Class<T> clazz) {
           throw new RuntimeException("No message decoder found for message: " + message);
        }
    };

    public IMessageDecoderFactory setDefaultMessageDecoder(IMessageDecoder defaultMessageDecoder) {
        this.defaultMessageDecoder=defaultMessageDecoder;
        return this;
    }

    @Override
    public IMessageDecoder find(Message<?> message) {
        return this.messageDecoders.stream().filter((decoder) -> decoder.support(message)).findFirst().orElse(this.defaultMessageDecoder);
    }

    @Override
    public IMessageDecoderFactory add(IMessageDecoder messageDecoder) {
        this.messageDecoders.add(messageDecoder);
        return this;
    }

    @Override
    public <T> Message<T> decode(Message<?> message, Class<T> clazz) {
        if (message == null){
            return null;
        }
        IMessageDecoder messageDecoder = this.find(message);
        if (messageDecoder != null){
            return messageDecoder.decode(message, clazz);
        }
        throw new RuntimeException("No message decoder found for message: " + message);
    }
}
