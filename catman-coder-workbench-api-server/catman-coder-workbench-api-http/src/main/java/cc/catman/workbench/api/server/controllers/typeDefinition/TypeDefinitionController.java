package cc.catman.workbench.api.server.controllers.typeDefinition;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import cc.catman.coder.workbench.core.SimpleInfo;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import org.springframework.web.bind.annotation.*;

import cc.catman.coder.workbench.core.type.TypeDefinition;

@RestController
@RequestMapping("type-definition")
public class TypeDefinitionController {
//    @Resource
//    private TypeDefinitionService typeDefinitionService;

    @Resource
    private ITypeDefinitionService typeDefinitionService;
    @GetMapping("/{id}")
    public TypeDefinition findById(@PathVariable String id) {
        return typeDefinitionService.findById(id).orElse(null);
    }

    @GetMapping("/fuzzy")
    public List<TypeDefinition> fuzzyQuery(@RequestParam(required = false) String key,
            @RequestParam(required = false) String[] fields) {
        return typeDefinitionService.list(null);
//        return typeDefinitionService.fuzzyQuery(FuzzyQuery.builder()
//                .key(key)
//                .fields(Optional.ofNullable(fields).map(Arrays::asList).orElse(Collections.emptyList()))
//                .build());
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

    /**
     * 判断一个类型是否可以直接转换为另一种类型
     * @param typeDefinition 类型定义
     * @param target 目标类型定义
     * @return 是否可以转换
     */
    @PostMapping("/tools/is-type")
    public boolean canBeAssigned(TypeDefinition typeDefinition,TypeDefinition target) {
        return typeDefinitionService.canBeAssigned(typeDefinition,target);
    }
}
