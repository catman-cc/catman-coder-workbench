package cc.catman.coder.workbench.core.apis.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationItemsRepository extends MongoRepository<ConfigurationItem,String> {

}
