package cc.catman.workbench.service.core.repossitory.executor.joincode;

import cc.catman.workbench.service.core.po.executor.joincode.IPStrategyRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIPStrategyRepository extends IBaseRepository<IPStrategyRef,String> {
}
