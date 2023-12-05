package cc.catman.workbench.api.server.controllers.parameter;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.workbench.service.core.services.IParameterService;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("parameter")
public class ParameterController {

    @Resource
    private IParameterService parameterService;

    @Resource
    private ITypeDefinitionService typeDefinitionService;
    @PostMapping("create-from-type-definition")
    public Parameter createFromTypeDefinition(@RequestBody TypeDefinition typeDefinition){
        return parameterService.createFromTypeDefinition(typeDefinition).orElse(null);
    }
    @PostMapping("create-from-type-definition/{id}")
    public Parameter createFromTypeDefinition(@PathVariable String id){
        return typeDefinitionService.findById(id).flatMap(typeDefinition -> parameterService.createFromTypeDefinition(typeDefinition)).orElse(null);
    }
}
