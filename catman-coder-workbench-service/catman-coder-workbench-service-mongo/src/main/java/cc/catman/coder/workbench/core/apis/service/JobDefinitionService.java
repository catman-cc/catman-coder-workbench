package cc.catman.coder.workbench.core.apis.service;

import java.util.List;

import cc.catman.coder.workbench.core.core.job.JobDefinition;

public interface JobDefinitionService extends BaseService<JobDefinition,String > {

    List<JobDefinition> list();
}
