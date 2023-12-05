//package cc.catman.workbench.api.server.controllers;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import javax.annotation.Resource;
//
//import cc.catman.coder.workbench.core.apis.service.FuzzyQuery;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import cc.catman.coder.workbench.core.apis.service.JobDefinitionService;
//import cc.catman.coder.workbench.core.job.JobDefinition;
//
///**
// * 任务定义控制器
// */
//@RestController
//@RequestMapping("/job-definition")
//public class JobDefinitionController {
//
//    @Resource
//    private JobDefinitionService jobDefinitionService;
//
//    @GetMapping("/")
//    public List<JobDefinition> list() {
//        return jobDefinitionService.list();
//    }
//
//    @GetMapping("/fuzzy")
//    public List<JobDefinition> fuzzyQuery(@RequestParam(required = false) String key,
//                                           @RequestParam(required = false) String[] fields) {
//
//        return jobDefinitionService.fuzzyQuery(FuzzyQuery.builder()
//                .key(key)
//                .fields(Optional.ofNullable(fields).map(Arrays::asList).orElse(Collections.emptyList()))
//                .build());
//    }
//}
