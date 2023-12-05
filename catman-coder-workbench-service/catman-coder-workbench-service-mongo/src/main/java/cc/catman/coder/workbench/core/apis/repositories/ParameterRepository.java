package cc.catman.coder.workbench.core.apis.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import cc.catman.coder.workbench.core.core.parameter.Parameter;

public interface ParameterRepository extends MongoRepository<Parameter, String> {
}
