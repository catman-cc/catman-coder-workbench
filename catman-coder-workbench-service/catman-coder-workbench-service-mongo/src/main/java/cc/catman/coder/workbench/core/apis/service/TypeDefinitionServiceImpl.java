package cc.catman.coder.workbench.core.apis.service;

import cc.catman.coder.workbench.core.apis.demos.TypeDefinitionReference;
import cc.catman.coder.workbench.core.apis.repositories.TypeDefinitionRepository;
import cc.catman.coder.workbench.core.core.SimpleInfo;
import cc.catman.coder.workbench.core.core.type.TypeDefinition;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
@Service
public class TypeDefinitionServiceImpl implements TypeDefinitionService {
    @Resource
    private MongoOperations mongoOperations;

    @Resource
    private TypeDefinitionRepository typeDefinitionRepository;

    public TypeDefinition save(TypeDefinition typeDefinition){
        return typeDefinitionRepository.save(typeDefinition);
    }

    public TypeDefinition findById(Object id){
        return mongoOperations.findById(id,TypeDefinition.class);
    }

    @Override
    public List<TypeDefinition> list(TypeDefinition typeDefinition) {
        return typeDefinitionRepository.findAll(Example.of(typeDefinition));
    }

    public List<SimpleInfo> listSimple(){
        return  mongoOperations.findAll(SimpleInfo.class,"typeDefinition");
    }

    @Override
    public List<TypeDefinition> fuzzyQuery(FuzzyQuery fuzzyQuery) {
        if (fuzzyQuery.isValid()){
            return mongoOperations.find(fuzzyQuery.toQuery(TypeDefinition.class),TypeDefinition.class);
        }
        return typeDefinitionRepository.findAll();
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
                                                .is(id)
                                )
                )
                , TypeDefinitionReference.class
        );
    }
}
