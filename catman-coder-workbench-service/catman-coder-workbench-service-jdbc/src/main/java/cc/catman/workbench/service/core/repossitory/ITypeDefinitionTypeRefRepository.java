package cc.catman.workbench.service.core.repossitory;

import cc.catman.workbench.service.core.po.TypeDefinitionTypeRef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeDefinitionTypeRefRepository extends JpaRepository<TypeDefinitionTypeRef,String > {
}
