package cc.catman.coder.workbench.core.value;

/**
 * 值提供者工厂
 */
public interface ValueProviderFactory {

    ValueProvider createValueProvider(ValueProviderDefinition valueProviderDefinition,ValueProviderRegistry valueProviderRegistry,ValueProviderContext context);
}
