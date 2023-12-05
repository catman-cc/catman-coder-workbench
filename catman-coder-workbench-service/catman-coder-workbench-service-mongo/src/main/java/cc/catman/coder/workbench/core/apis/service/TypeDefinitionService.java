package cc.catman.coder.workbench.core.apis.service;

import cc.catman.coder.workbench.core.core.SimpleInfo;
import cc.catman.coder.workbench.core.core.type.TypeDefinition;

import java.util.List;

public interface TypeDefinitionService {
    TypeDefinition save(TypeDefinition typeDefinition);

    TypeDefinition findById(Object id);

    List<TypeDefinition> list(TypeDefinition typeDefinition);

    List<TypeDefinition> fuzzyQuery(FuzzyQuery fuzzyQuery);

    long count(String id);

    List<SimpleInfo> listSimple();
}
