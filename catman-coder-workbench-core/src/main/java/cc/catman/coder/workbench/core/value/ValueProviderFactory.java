package cc.catman.coder.workbench.core.value;

/**
 * 值提供者工厂
 */
public interface ValueProviderFactory<C extends ValueProviderConfig,T extends ValueProvider<C>> {

    /**
     * 获取值提供者
     * @param provider 值提供者
     * @return 值提供者
     */
    T  getValueProvider(DefaultValueProvider provider);

    T save(T valueProvider, ValueProviderManager manager);
}
