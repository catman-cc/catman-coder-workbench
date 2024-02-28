package cc.catman.workbench.api.server.controllers.parameter;

import cc.catman.coder.workbench.core.parameter.Parameter;
import cc.catman.coder.workbench.core.parameter.ParameterSchema;
import cc.catman.coder.workbench.core.type.TypeDefinition;
import cc.catman.workbench.service.core.services.IParameterService;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

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
    public ParameterSchema createFromTypeDefinition(@PathVariable String id){
        return typeDefinitionService.findById(id).map(typeDefinition -> parameterService.create(typeDefinition))
                .map(ParameterSchema::of)
                .orElse(null);
    }

}
