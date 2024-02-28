package cc.catman.workbench.service.core.repossitory.valueProvider;

import cc.catman.workbench.service.core.po.valueProvider.ValueProviderDefinitionRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IValueProviderDefinitionRefRepository extends IBaseRepository<ValueProviderDefinitionRef,String> {
}
