package cc.catman.workbench.service.core.repossitory.parameter;

import cc.catman.workbench.service.core.po.parameter.ParameterRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IParameterRefRepository extends IBaseRepository<ParameterRef,String> {
}
