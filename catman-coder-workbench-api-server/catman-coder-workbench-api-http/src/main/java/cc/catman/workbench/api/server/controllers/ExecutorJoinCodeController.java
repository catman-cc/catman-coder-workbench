package cc.catman.workbench.api.server.controllers;

import cc.catman.coder.workbench.core.executor.ExecutorJoinCode;
import cc.catman.coder.workbench.core.executor.ExecutorJoinCodeStatus;
import cc.catman.workbench.api.server.configuration.page.Page;
import cc.catman.workbench.service.core.common.page.PageParam;
import cc.catman.workbench.service.core.common.page.VPage;
import cc.catman.workbench.service.core.services.IExecutorJoinCodeService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("join-code")
public class ExecutorJoinCodeController {

    @Resource
    private IExecutorJoinCodeService executorJoinCodeService;

    @GetMapping("/{id}")
    public ExecutorJoinCode findById(@PathVariable String id){
        return executorJoinCodeService.findById(id).orElse(null);
    }

    @GetMapping("/all")
    public List<ExecutorJoinCode> findAll(@RequestParam(required = false,name = "keyword") String keyword){
        return executorJoinCodeService.findAll(keyword);
    }

    @GetMapping("/page")
    public VPage<ExecutorJoinCode> fuzzy(@Page PageParam page
            , @RequestParam(required = false,name = "name") String name
, @RequestParam(required = false,name = "code") String code
, @RequestParam(required = false,name = "key") String key
, @RequestParam(required = false,name = "states") List<ExecutorJoinCodeStatus> states){
        return executorJoinCodeService.fuzzy(page,name,code,key,states);
    }

    @GetMapping("/page/fuzzy")
    public VPage<ExecutorJoinCode> fuzzy(@Page PageParam page,@RequestParam(name = "keyword") String keyword){
        return executorJoinCodeService.fuzzy(page,keyword);
    }

    @GetMapping("/create-join-code")
    public String createJoinCode(){
        return executorJoinCodeService.createJoinCode();
    }

    @GetMapping("/join-code/{joinCode}")
    public ExecutorJoinCode findByJoinCode(@PathVariable String joinCode){
        return executorJoinCodeService.findByJoinCode(joinCode).orElse(null);
    }

    @PutMapping("/create")
    public ExecutorJoinCode createExecutorJoinCode(){
        return executorJoinCodeService.createExecutorJoinCode();
    }

    @PutMapping("/flush-join-code")
    public ExecutorJoinCode flushJoinCode(@RequestBody ExecutorJoinCode executorJoinCode){
        return executorJoinCodeService.flushJoinCode(executorJoinCode);
    }

    @PutMapping("/flush-join-code/{id}")
    public ExecutorJoinCode onlyFlushJoinCode(@PathVariable String id){
        return executorJoinCodeService.onlyFlushJoinCode(id);
    }
    @PutMapping("/save")
    public ExecutorJoinCode save(@RequestBody ExecutorJoinCode executorJoinCode){
        return executorJoinCodeService.save(executorJoinCode);
    }
}
