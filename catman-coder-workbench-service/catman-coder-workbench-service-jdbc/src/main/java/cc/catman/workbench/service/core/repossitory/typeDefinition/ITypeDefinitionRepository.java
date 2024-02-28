package cc.catman.workbench.service.core.repossitory.typeDefinition;

import cc.catman.workbench.service.core.po.typeDefintion.TypeDefinitionPO;
import cc.catman.workbench.service.core.repossitory.base.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypeDefinitionRepository extends IBaseRepository<TypeDefinitionPO,String> {
}
