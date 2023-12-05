package cc.catman.coder.workbench.core.apis.repositories;

import cc.catman.coder.workbench.core.core.type.TypeDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeDefinitionRepository extends MongoRepository<TypeDefinition,String> {
}
