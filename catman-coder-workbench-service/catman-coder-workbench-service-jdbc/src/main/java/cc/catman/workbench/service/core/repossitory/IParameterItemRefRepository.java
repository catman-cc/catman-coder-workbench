package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ParameterItemRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IParameterItemRefRepository extends JpaRepository<ParameterItemRef,String> {
}
