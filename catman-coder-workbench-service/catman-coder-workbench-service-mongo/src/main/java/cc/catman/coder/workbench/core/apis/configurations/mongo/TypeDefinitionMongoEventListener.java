package cc.catman.coder.workbench.core.apis.configurations.mongo;

import java.util.Objects;

import javax.annotation.Resource;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mongodb.client.result.DeleteResult;

import cc.catman.coder.workbench.core.apis.configurations.mongo.cascade.core.CascadeHelper;
import cc.catman.coder.workbench.core.apis.demos.TypeDefinitionReference;
import cc.catman.coder.workbench.core.core.type.TypeDefinition;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于自动维护TypeDefinition之间的关系
 */
@Slf4j
@Component
public class TypeDefinitionMongoEventListener extends AbstractMongoEventListener<TypeDefinition> {

    @Resource
    private MongoOperations mongoOperations;

    @Override
    public void onBeforeSave(BeforeSaveEvent<TypeDefinition> event) {
        TypeDefinition source = event.getSource();
        TypeDefinitionReference save = mongoOperations.save(
                TypeDefinitionReference.builder()
                        .id(source.getId())
                        .items(source.recursionListPublic().stream().map(TypeDefinition::getId).toList())
                        .build());
        log.debug("auto update parameter references:{}", save);
    }

    @Override
    public void onAfterDelete(AfterDeleteEvent<TypeDefinition> event) {
        Class<TypeDefinition> type = Objects.requireNonNull(event.getType());
        Document document = Objects.requireNonNull(event.getDocument());
        String idName = CascadeHelper.findIdName(type);
        if (!StringUtils.hasText(idName)) {
            throw new RuntimeException(
                    String.format("The id field in the collection %s cannot be found. " +
                            "Please add @MongoId or @ID annotation to the id field of the collection " +
                            "according to the specification.", type.getCanonicalName()));
        }
        Object id = document.get(idName);
        DeleteResult remove = mongoOperations.remove(
                Query.query(Criteria
                        .where(idName)
                        .is(id)),
                TypeDefinitionReference.class);
        log.debug("auto delete parameter references: id:{},count:{}", id, remove.getDeletedCount());
    }
}
