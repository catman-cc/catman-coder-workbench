package cc.catman.coder.workbench.core.value;

import cc.catman.coder.workbench.core.value.providers.UniversalValueProviderConfig;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Optional;

/**
 * 值提供者,用于上下文中读取相关的值
 */
public interface ValueProvider {
    /**
     * 值提供者的唯一标识
     */
    String getId();

    void setId(String id);
    /**
     * 值提供者的类型,比如:
     * simple: 直接复制
     * reference: 引用其他的值
     * env: 从环境变量中读取
     * if: 条件判断取值
     * switch: 多条件判断取值
     * random: 随机取值
     * sequence: 顺序取值
     * range: 范围取值
     * regex: 正则取值
     * script: 脚本取值
     */
    String getName();
    String getKind();
    void setKind(String kind);

    UniversalValueProviderConfig getConfig();

    void setConfig(UniversalValueProviderConfig config);

    Optional<Object> run(ValueProviderContext context);

default <R> Optional<R> run(ValueProviderContext context, Class<R> type) {
    Object res = this.run(context);
    return context.convert(res, TypeDescriptor.valueOf(type));
}
default <R> R run(ValueProviderContext context, Class<R> type, R defaultValue) {
        return this.run(context, type).orElse(defaultValue);
    }
}
