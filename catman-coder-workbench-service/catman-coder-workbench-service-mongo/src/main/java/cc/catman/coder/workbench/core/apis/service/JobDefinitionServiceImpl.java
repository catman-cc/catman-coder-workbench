package cc.catman.coder.workbench.core.apis.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.Resource;

import cc.catman.coder.workbench.core.apis.repositories.JobDefinitionRepository;
import org.springframework.stereotype.Service;

import cc.catman.coder.workbench.core.core.job.JobDefinition;

@Service
public class JobDefinitionServiceImpl implements JobDefinitionService {

    @Resource
    private JobDefinitionRepository repository;

    @Override
    public List<JobDefinition> list() {
        return Optional.ofNullable(repository.findAll()).orElse(Collections.emptyList());
    }

    @Override
    public JobDefinition save(JobDefinition jobDefinition) {
        return repository.save(jobDefinition);
    }

    @Override
    public JobDefinition findById(String s) {
//        return repository.findById(s);
        return null;
    }

    @Override
    public List<JobDefinition> list(JobDefinition parameter) {
        return null;
    }

    @Override
    public List<JobDefinition> fuzzyQuery(FuzzyQuery fuzzyQuery) {
        return repository.findAll();
    }
}
