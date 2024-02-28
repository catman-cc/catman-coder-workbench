package cc.catman.coder.workbench.core.apis.controllers;

import java.util.List;

import jakarta.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.catman.coder.workbench.core.apis.service.FuzzyQuery;
import cc.catman.coder.workbench.core.apis.service.ParameterService;
import cc.catman.coder.workbench.core.core.parameter.Parameter;

@RequestMapping("parameter")
@RestController
public class ParameterController {
    @Resource
    private ParameterService parameterService;

    @GetMapping("/")
    public List<Parameter> list() {
        return parameterService.list(null);
    }

    @GetMapping("/{id}")
    public Parameter findById(@PathVariable String id) {
        return parameterService.findById(id);
    }

    @GetMapping("/fuzzy")
    public List<Parameter> fuzzyQuery(@RequestParam("key") String key) {
        return parameterService.fuzzyQuery(FuzzyQuery.builder().key(key).build());
    }

    @GetMapping("/reference/count/{id}")
    public long countReference(@PathVariable String id) {
        return parameterService.count(id);
    }

    @PostMapping()
    public Parameter save(@RequestBody Parameter parameter) {
        return parameterService.save(parameter);
    }
}
