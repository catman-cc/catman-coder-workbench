package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.ParameterTypeDefinitionRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IParameterTypeDefinitionRefRepository extends JpaRepository<ParameterTypeDefinitionRef,String> {
}
