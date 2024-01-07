package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.type.TypeDefinitionSchema;

import java.util.Optional;

public interface ITypeDefinitionSchemaService {
    /**
     * 保存类型定义
     * @param schema 类型定义
     * @return 保存后的类型定义
     */
    TypeDefinitionSchema save(TypeDefinitionSchema schema);

    /**
     * 根据id查询类型定义
     * @param id 类型定义id
     * @return 类型定义
     */
    Optional<TypeDefinitionSchema> findById(String id);
}
