package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.SimpleInfo;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.ILoopReferenceContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 类型定义服务
 * 因为操作复杂,此处最好设计缓存
 */
public interface ITypeDefinitionService {
    /**
     * 保存类型定义
     * @param typeDefinition 类型定义
     * @return 保存后的类型定义
     */
    default TypeDefinition save(TypeDefinition typeDefinition){
        return save(typeDefinition, Optional.ofNullable(typeDefinition).map(TypeDefinition::getContext).orElse(ILoopReferenceContext.create()));
    }

    TypeDefinition save(TypeDefinition typeDefinition, ILoopReferenceContext context);

    /**
     * 根据id查询类型定义
     * @param id 类型定义id
     * @return 类型定义
     */
    Optional<TypeDefinition> findById(String id);


    Optional<TypeDefinition> findById(String id, ILoopReferenceContext context);

    /**
     * 根据id删除类型定义, 删除类型定义时,需要考虑类型定义的范围,如果类型定义是PRIVATE,那么可以删除,如果类型是PUBLIC,在级联状态下,删除级联关系即可,
     * @param id 类型定义id
     * @return 类型定义
     */
    Optional<TypeDefinition> deleteById(String id);

    Optional<TypeDefinition> deleteByBelongId(String belongId);

    /**
     * 根据类型定义查询类型定义
     * @param typeDefinition 类型定义
     * @return 类型定义
     */
    List<TypeDefinition> list(TypeDefinition typeDefinition);

    /**
     * 根据id统计类型定义
     * @param id 类型定义id
     * @return 类型定义数量
     */
    long count(String id);

    /**
     * 查询所有类型定义
     * @return 类型定义
     */
    List<SimpleInfo> listSimple();

    /**
     * 更新类型定义的scope
     * @param id 类型定义id
     * @param scope 类型定义scope
     * @return 是否更新成功
     */
    boolean updateScope(String id, String scope);

    boolean canBeAssigned(TypeDefinition typeDefinition,TypeDefinition target);

    /**
     * 查询直接引用了该类型定义的类型定义
     * @param id 类型定义id
     * @return 类型定义
     */
    List<TypeDefinition> findDirectReferencedById(String id);
}
