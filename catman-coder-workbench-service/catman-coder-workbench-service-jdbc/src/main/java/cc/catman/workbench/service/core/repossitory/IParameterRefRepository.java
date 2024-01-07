package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ParameterRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IParameterRefRepository extends JpaRepository<ParameterRef,String> {
}
