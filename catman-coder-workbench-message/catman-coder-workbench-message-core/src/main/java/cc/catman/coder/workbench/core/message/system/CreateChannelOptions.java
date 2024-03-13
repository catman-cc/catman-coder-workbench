package cc.catman.coder.workbench.core.message.system;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 创建信道
 */
@Data
@Builder
public class CreateChannelOptions {
    /**
     * 信道名称
     */
    private String channelId;

    private String name;

    /**
     * 信道类型
     */
    private String kind;
    /**
     * 如果已经存在了同类型,同名的信道,是否覆盖
     */
    private boolean overWrite;

    /**
     * 信道属性
     */
    private Map<String,Object> attributes;
}
