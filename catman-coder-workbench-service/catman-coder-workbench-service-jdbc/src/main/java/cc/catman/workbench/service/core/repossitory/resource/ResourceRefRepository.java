package cc.catman.workbench.service.core.repossitory.resource;

import cc.catman.workbench.service.core.po.resource.ResourceRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRefRepository extends IBaseRepository<ResourceRef,String> {
}
