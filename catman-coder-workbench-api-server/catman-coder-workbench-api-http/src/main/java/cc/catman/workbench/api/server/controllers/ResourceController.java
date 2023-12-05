package cc.catman.workbench.api.server.controllers;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;
import cc.catman.workbench.service.core.services.IResourceService;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import cc.catman.workbench.service.core.services.ResourceDetailsLoaderManager;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 资源控制器
 */
@Slf4j
@RestController
@RequestMapping("resource")
public class ResourceController {
    @javax.annotation.Resource
    private ModelMapper modelMapper;
    @javax.annotation.Resource
    private IResourceService resourceService;

    @javax.annotation.Resource
    private ResourceDetailsLoaderManager resourceDetailsLoaderManager;

    /**
     * 获取根资源,及其子资源
     */
    @GetMapping("/root")
    public Resource root(){
        return resourceService.findRoot(-1);
    }

    /**
     * 获取资源及其所有子资源
     */
    @GetMapping("/{id}")
    public Resource findById(@PathVariable String id){
        return resourceService.findById(id,-1);
    }

    /**
     * 获取资源详情
     * @param resourceId 资源id
     * @return 资源详情
     */
    @GetMapping("/details/{resourceId}")
    public Object findResourceDetails(@PathVariable String resourceId){
        Resource resource = resourceService.findById(resourceId,0);
        ResourceDetails details=modelMapper.map(resource,ResourceDetails.class);
        resourceDetailsLoaderManager.find(resource).ifPresent(loader->details.setDetails(loader.load(resource)));
        return details;
    }

    /**
     * 保存资源,包括更新操作
     * @param resource 资源
     * @return 保存后的资源
     */
    @PutMapping
    public Resource save(@RequestBody Resource resource){
        return resourceService.save(resource);
    }


    @PutMapping("/create")
    public Resource create(@RequestBody ResourceCreate config){
        if ("resource".equals(config.getKind())){
           return resourceService.save(config);
        }

        Optional<ResourceDetailsLoader> loader = resourceDetailsLoaderManager.find(config);
        if(loader.isPresent()){
            ResourceDetails details = loader.get().create(config);
            Resource save = resourceService.save(details);
            ResourceDetails res=modelMapper.map(save,ResourceDetails.class);
            res.setDetails(details.getDetails());
            return  res;
        }
        return null;
    }

    /**
     * 删除资源
     * @param id 资源id
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable String id){
        // 加载资源及其子资源
        resourceService.deepWithHandler(id,resource->{
            // 删除资源的同时删除相关资源
            log.info("delete resource:{}",resource);
            resourceDetailsLoaderManager.find(resource).ifPresent(loader->loader.delete(resource));
            resourceService.deleteById(resource.getId());
        });
        return true;
    }
}
