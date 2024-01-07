package cc.catman.workbench.api.server.controllers.typeDefinition;

import java.util.List;

import javax.annotation.Resource;

import cc.catman.coder.workbench.core.SimpleInfo;
import cc.catman.coder.workbench.core.type.TypeDefinitionSchema;
import cc.catman.workbench.service.core.services.ITypeDefinitionSchemaService;
import cc.catman.workbench.service.core.services.ITypeDefinitionService;
import org.springframework.web.bind.annotation.*;

import cc.catman.coder.workbench.core.type.TypeDefinition;

@RestController
@RequestMapping("type-definition")
public class TypeDefinitionController {
    @Resource
    private ITypeDefinitionService typeDefinitionService;
    @Resource
    private ITypeDefinitionSchemaService typeDefinitionSchemaService;
    @GetMapping("/{id}")
    public TypeDefinition findById(@PathVariable String id) {
        return typeDefinitionService.findById(id).orElse(null);
    }

    @GetMapping("/fuzzy")
    public List<TypeDefinition> fuzzyQuery(@RequestParam(required = false) String key,
            @RequestParam(required = false) String[] fields) {
        return typeDefinitionService.list(null);
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

    @PutMapping("schema")
    public TypeDefinitionSchema save(@RequestBody TypeDefinitionSchema schema) {
        // 接下来开始执行保存操作
        return typeDefinitionSchemaService.save(schema);
    }

    @GetMapping("schema/{id}")
    public TypeDefinitionSchema findSchemaById(@PathVariable String id){
        return typeDefinitionSchemaService.findById(id).orElse(null);
    }
}
