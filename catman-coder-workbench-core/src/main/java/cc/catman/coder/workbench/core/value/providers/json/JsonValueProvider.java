package cc.catman.coder.workbench.core.value.providers.json;

import cc.catman.coder.workbench.core.value.providers.AbstractValueProvider;
import cc.catman.coder.workbench.core.value.ValueProviderContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

/**
 * JSON取值器
 */
@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class JsonValueProvider extends AbstractValueProvider {
    @Builder.Default
    private String kind="json";

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

    private ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Optional<Object> run(ValueProviderContext context) {
        // 按理来说此时就可以返回对象的类型了,如果交给上游处理,会多处理一次
        // 那么此时就需要解析result的定义,然后获取具体类型吗?
        // 但是也说不准,除非选择递归构建,但是如果返回类型属于动态类型定义内嵌java类型,依然无法直接反序列化
        // TODO:  #待验证# #优化# 字节码缓存动态类型定义是否可以降低序列化成本,以及使用字节码缓存的负面效果
        // 基于动态字节码创建类型,也不会快很多,但是可以考虑为TypeDefinition基于字节码创建类型,然后缓存
        return Optional.ofNullable(objectMapper.readValue(value, Object.class));
    }
}
