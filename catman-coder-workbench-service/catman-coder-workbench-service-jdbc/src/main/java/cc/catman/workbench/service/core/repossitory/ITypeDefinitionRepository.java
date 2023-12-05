package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.TypeDefinitionPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeDefinitionRepository extends JpaRepository<TypeDefinitionPO,String> {
}
