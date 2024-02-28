package cc.catman.workbench.service.core.repossitory.valueProvider;

import cc.catman.workbench.service.core.po.valueProvider.ValueProviderParameterRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValueProviderConfigRefRepository extends IBaseRepository<ValueProviderParameterRef,String> {
}
