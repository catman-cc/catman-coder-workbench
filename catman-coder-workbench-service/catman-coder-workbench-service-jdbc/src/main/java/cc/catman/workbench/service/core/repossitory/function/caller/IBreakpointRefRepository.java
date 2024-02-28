package cc.catman.workbench.service.core.repossitory.function.caller;

import cc.catman.workbench.service.core.po.function.caller.BreakpointRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBreakpointRefRepository extends IBaseRepository<BreakpointRef,String > {
}
