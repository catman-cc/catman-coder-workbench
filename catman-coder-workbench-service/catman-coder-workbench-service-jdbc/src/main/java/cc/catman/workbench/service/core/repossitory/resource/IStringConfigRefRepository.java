package cc.catman.workbench.service.core.repossitory.resource;

import cc.catman.workbench.service.core.po.resource.StringConfigPO;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStringConfigRefRepository extends IBaseRepository<StringConfigPO,String> {
}
