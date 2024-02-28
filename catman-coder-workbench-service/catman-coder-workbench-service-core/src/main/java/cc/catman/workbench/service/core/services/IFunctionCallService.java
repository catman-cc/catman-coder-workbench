package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.ILoopReferenceContext;
import cc.catman.coder.workbench.core.function.FunctionCallInfo;

import java.util.Optional;

public interface IFunctionCallService {

   default Optional<FunctionCallInfo> findById(String id){
       return findById(id,ILoopReferenceContext.create());
   }

    Optional<FunctionCallInfo> findById(String id, ILoopReferenceContext context);
}
