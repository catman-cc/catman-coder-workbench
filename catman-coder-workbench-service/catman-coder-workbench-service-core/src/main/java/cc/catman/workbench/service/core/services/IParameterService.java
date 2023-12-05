package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;

import java.util.Optional;

/**
 * 参数定义服务
 */
public interface IParameterService {
    /**
     * 从现有的类型定义创建一个参数定义
     * @param typeDefinition 类型定义
     * @return 参数定义
     */
    Optional<Parameter> createFromTypeDefinition(TypeDefinition typeDefinition);

    /**
     * 根据id查找参数定义
     * @param id 参数定义id
     * @return 参数定义
     */
    Optional<Parameter> findById(String id);

    Parameter save(Parameter parameter);

    Optional<Parameter> deleteById(String id);
}
