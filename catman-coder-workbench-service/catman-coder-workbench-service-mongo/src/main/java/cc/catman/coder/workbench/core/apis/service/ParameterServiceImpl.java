package cc.catman.coder.workbench.core.apis.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.Resource;

import cc.catman.coder.workbench.core.apis.demos.TypeDefinitionReference;
import cc.catman.coder.workbench.core.apis.repositories.ParameterRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cc.catman.coder.workbench.core.core.parameter.Parameter;

@Service
public class ParameterServiceImpl implements ParameterService {
    @Resource
    private MongoOperations mongoOperations;

    @Resource
    private ParameterRepository parameterRepository;

    public Parameter save(Parameter parameter) {
        return parameterRepository.save(parameter);
    }

    public Parameter findById(String id) {
        return mongoOperations.findById(id, Parameter.class);
    }

    @Override
    public List<Parameter> list(Parameter parameter) {
        return Optional.ofNullable(parameter).map(p -> parameterRepository.findAll(Example.of(p)))
                .orElseGet(() -> parameterRepository.findAll());
    }

    @Override
    public List<Parameter> fuzzyQuery(FuzzyQuery fuzzyQuery) {
        List<Parameter> parameters = Optional.ofNullable(fuzzyQuery.getKey())
                .map(k -> {
                    Query query = new Query();
                    query.addCriteria(
                            Criteria.where("")
                                    .orOperator(
                                            Criteria.where("id").regex(k),
                                            Criteria.where("name").regex(k),
                                            Criteria.where("describe").regex(k),
                                            Criteria.where("alias")
                                                    .elemMatch(Criteria.where("$regex").is(k)))

                );
                    return mongoOperations.find(query, Parameter.class);
                }).orElseGet(() -> list(new Parameter()));
        return Optional.ofNullable(parameters).orElse(Collections.emptyList());

    }

    @Override
    public long count(String id) {
        return mongoOperations.count(
                Query.query(
                        Criteria
                                .where("items")
                                .elemMatch(
                                        Criteria
                                                .where("$eq")
                                                .is(id))),
                TypeDefinitionReference.class);
    }
}
