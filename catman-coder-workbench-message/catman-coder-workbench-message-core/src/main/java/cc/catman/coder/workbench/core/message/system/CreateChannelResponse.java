package cc.catman.coder.workbench.core.message.system;

import lombok.Builder;
import lombok.Data;

/**
 * 创建信道响应
 */
@Data
@Builder
public class CreateChannelResponse {
    /**
     * 是否成功
     */
    boolean success;
    /**
     * 通道id
     */
    String channelId;
    /**
     * 如果创建失败,此处提供原因
     */
    String reason;
}
