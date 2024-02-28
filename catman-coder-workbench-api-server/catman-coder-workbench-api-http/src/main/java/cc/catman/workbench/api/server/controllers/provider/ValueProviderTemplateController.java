package cc.catman.workbench.api.server.controllers.provider;

import cc.catman.coder.workbench.core.value.template.ValueProviderTemplate;
import cc.catman.workbench.service.core.services.IValueProviderDefinitionTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("value-provider-template")
public class ValueProviderTemplateController {
    @Resource
    private IValueProviderDefinitionTemplateService templateService;

    /**
     * 搜索模板
     * @param keyword 关键字
     * @return 模板列表
     */
    @GetMapping("search/{keyword}")
    public List<ValueProviderTemplate> search(@PathVariable String keyword){
        return templateService.search(keyword);
    }
}
