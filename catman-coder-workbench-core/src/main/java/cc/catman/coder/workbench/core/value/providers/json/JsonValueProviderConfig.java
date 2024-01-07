package cc.catman.coder.workbench.core.value.providers.json;

import cc.catman.coder.workbench.core.value.providers.DefaultValueProviderConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 通过解析json提供值
 */
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class JsonValueProviderConfig extends DefaultValueProviderConfig {
    /**
     * json来源:
     * 1. url请求
     * 2. raw 原始的json内容
     * 3. file 文件?
     */
    private String sourceKind;

    /**
     * 配合sourceKind使用
     * 如果sourceKind为url,则该值为url地址
     * 如果sourceKind为raw,则该值为json内容
     * 如果sourceKind为file,则该值为文件路径
     */
    private String value;
}
