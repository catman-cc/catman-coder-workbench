package cc.catman.coder.workbench.core.apis.controllers.typeDefinition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.Resource;

import cc.catman.coder.workbench.core.core.SimpleInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.catman.coder.workbench.core.apis.service.FuzzyQuery;
import cc.catman.coder.workbench.core.apis.service.TypeDefinitionService;
import cc.catman.coder.workbench.core.core.type.TypeDefinition;

@RestController
@RequestMapping("type-definition")
public class TypeDefinitionController {
    @Resource
    private TypeDefinitionService typeDefinitionService;

    @GetMapping("/{id}")
    public TypeDefinition findById(@PathVariable String id) {
        return typeDefinitionService.findById(id);
    }

    @GetMapping("/fuzzy")
    public List<TypeDefinition> fuzzyQuery(@RequestParam(required = false) String key,
            @RequestParam(required = false) String[] fields) {
        return typeDefinitionService.fuzzyQuery(FuzzyQuery.builder()
                .key(key)
                .fields(Optional.ofNullable(fields).map(Arrays::asList).orElse(Collections.emptyList()))
                .build());
    }

    @GetMapping("/list-simple")
    public List<SimpleInfo> simple() {
        return typeDefinitionService.listSimple();
    }

    @PutMapping
    public TypeDefinition save(@RequestBody TypeDefinition typeDefinition) {
        return typeDefinitionService.save(typeDefinition);
    }

    @GetMapping("/reference/count/{id}")
    public long countReference(@PathVariable String id) {
        return typeDefinitionService.count(id);
    }
}
