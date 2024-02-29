package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;
import cc.catman.coder.workbench.core.function.FunctionInfo;

import java.util.Optional;

/**
 * 函数调用信息服务
 */
public interface IFunctionCallService {

   default Optional<FunctionCallInfo> findById(String id){
       return findById(id,ILoopReferenceContext.create());
   }

    Optional<FunctionCallInfo> findById(String id, ILoopReferenceContext context);

   FunctionCallInfo save(FunctionCallInfo functionCallInfo);

   /**
    * 创建一个函数调用信息
    * @param functionInfo 函数信息
    * @param context 上下文
    * @return 函数调用信息
    */
   FunctionCallInfo create(FunctionInfo functionInfo,ILoopReferenceContext context);

   /**
    * 创建一个函数调用信息
    * @param functionInfo 函数信息
    * @return 函数调用信息
    */
  default FunctionCallInfo create(FunctionInfo functionInfo){
      return create(functionInfo,Optional.ofNullable(functionInfo.getContext()).orElseGet(ILoopReferenceContext::create));
  }
}
