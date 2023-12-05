package cc.catman.coder.workbench.core.apis.service;


import cc.catman.coder.workbench.core.core.parameter.Parameter;

public interface ParameterService extends BaseService<Parameter,String>{
    long count(String id);
}
