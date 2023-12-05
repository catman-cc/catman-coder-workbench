package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ResourceRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRefRepository extends JpaRepository<ResourceRef,String> {
}
