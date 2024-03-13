package cc.catman.coder.workbench.core.message.client;

import cc.catman.coder.workbench.core.message.MessageChannel;
import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;
import lombok.Data;

@Data
public class MessageChannelHolder {
    /**
     * 创建channel的参数
     */
    private CreateChannelOptions options;
    /**
     * 消息通道
     */
    private MessageChannel channel;
    /**
     * 消息通道状态
     */
    private MessageChannelStatus status;
    /**
     * 通道建立完毕后的回调方法
     */
    private ICreateChannelCallback callback;
}
