package cc.catman.workbench.service.core.repossitory.parameter;

import cc.catman.workbench.service.core.po.parameter.ParameterItemRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IParameterItemRefRepository extends IBaseRepository<ParameterItemRef,String> {
}
