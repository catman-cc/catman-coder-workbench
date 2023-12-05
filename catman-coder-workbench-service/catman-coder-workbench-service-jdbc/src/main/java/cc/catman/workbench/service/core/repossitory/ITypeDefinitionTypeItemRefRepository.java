package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.TypeDefinitionTypeItemRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeDefinitionTypeItemRefRepository extends JpaRepository<TypeDefinitionTypeItemRef,String > {
}
