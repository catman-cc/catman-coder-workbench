package cc.catman.coder.workbench.core.value;

import java.util.List;

/**
 * 值提供者管理器
 */
public interface ValueProviderManager {

    List<ValueProviderInfo> listValueProviderInfo();

    <C extends ValueProviderConfig,T extends ValueProvider<C>> ValueProviderFactory<C,T> getValueProviderFactory(String kind);

}
