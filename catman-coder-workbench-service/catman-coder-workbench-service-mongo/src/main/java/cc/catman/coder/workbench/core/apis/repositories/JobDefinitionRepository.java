package cc.catman.coder.workbench.core.apis.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cc.catman.coder.workbench.core.core.job.JobDefinition;

@Repository
public interface JobDefinitionRepository extends MongoRepository<JobDefinition, String> {

}
