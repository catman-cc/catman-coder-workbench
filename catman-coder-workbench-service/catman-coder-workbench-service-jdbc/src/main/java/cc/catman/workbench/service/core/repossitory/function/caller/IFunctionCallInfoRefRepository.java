package cc.catman.workbench.service.core.repossitory.function.caller;

import cc.catman.workbench.service.core.po.function.caller.FunctionCallInfoRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

/**
 * 函数调用信息仓库
 */
@Repository
public interface IFunctionCallInfoRefRepository extends IBaseRepository<FunctionCallInfoRef,String> {
}
