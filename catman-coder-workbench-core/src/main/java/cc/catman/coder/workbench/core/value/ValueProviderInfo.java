package cc.catman.coder.workbench.core.value;

import lombok.Data;

/**
 * 值提供者信息
 */
@Data
public class ValueProviderInfo {
    /**
     * 值提供者名称
     */
    private String name;
    /**
     * 值提供者类型
     */
    private String kind;

    /**
     * 值提供者描述
     */
    private String describe;
}
