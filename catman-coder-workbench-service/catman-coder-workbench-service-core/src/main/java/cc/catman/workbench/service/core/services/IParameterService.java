package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.coder.workbench.core.value.ValueProviderDefinition;
import cc.catman.coder.workbench.core.ILoopReferenceContext;

import java.util.Map;
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

    Optional<Parameter> findById(String id, ILoopReferenceContext context);


    Optional<Parameter> findById(String id, Map<String, ValueProviderDefinition> existPublicValueProviderDefinitions, Map<String,Parameter> existPublicParameters, Map<String,TypeDefinition> existPublicTypeDefinitions);

    Parameter save(Parameter parameter);

    default Optional<Parameter> deleteById(String id){
        return deleteById(id,false);
    }
    default Optional<Parameter> deleteById(String id,boolean includePublic){
        return deleteById(id,0,false);
    }
    default Optional<Parameter> deleteById(String id,int stackCount,boolean includePublic){
        return this.findById(id).map(ifp->this.delete(ifp,stackCount,includePublic)?ifp:null);
    }

   default boolean delete(Parameter parameter){
       return delete(parameter,false);
   }

    default boolean delete(Parameter parameter,boolean includePublic){
        return delete(parameter,0,includePublic);
    }

    boolean delete(Parameter parameter,int stackCount,boolean includePublic);

}
