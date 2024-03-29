package cc.catman.workbench.api.server.controllers;

import cc.catman.coder.workbench.core.label.DefaultLabelSelectorContext;
import cc.catman.coder.workbench.core.label.DefaultLabelSelectorFactory;
import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.entity.ResourceCreate;
import cc.catman.workbench.service.core.entity.ResourceDetails;
import cc.catman.workbench.service.core.services.IResourceService;
import cc.catman.workbench.service.core.services.ResourceDetailsLoader;
import cc.catman.workbench.service.core.services.ResourceDetailsLoaderManager;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 资源控制器
 */
@Slf4j
@RestController
@RequestMapping("resource")
public class ResourceController {
   @jakarta.annotation.Resource
    private ModelMapper modelMapper;
   @jakarta.annotation.Resource
    private IResourceService resourceService;

   @jakarta.annotation.Resource
    private ResourceDetailsLoaderManager resourceDetailsLoaderManager;

    private DefaultLabelSelectorContext labelSelectorContext=DefaultLabelSelectorContext.createDefault();
    /**
     * 获取根资源,及其子资源
     */
    @GetMapping("/root")
    public Resource root(@RequestParam(value = "selector",required = false)String selector){
        Resource root = resourceService.findRoot(-1);
        if(Optional.ofNullable(selector).filter(StringUtils::hasText).isPresent()){
           return root.filterAndNotEmpty(resource->labelSelectorContext.valid(selector,resource),modelMapper);
        }
        return root;
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

    @PutMapping("/rename/{id}/{name}")
    public Resource rename(@PathVariable String id,@PathVariable String name){
        resourceDetailsLoaderManager.find(resourceService.findById(id,0)).ifPresent(loader->loader.rename(resourceService.findById(id,0),name));
        return resourceService.rename(id,name);
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

    @PutMapping("/move/{id}")
    public boolean move(@PathVariable String id
            ,@RequestParam(name = "parentId") String parentId
            ,@RequestParam(required = false,name = "previousId") String previousId
    ,@RequestParam(required = false,name = "nextId") String nextId
                        ,@RequestParam(required = false,name = "index") Integer index){

        resourceService.move(id,parentId,previousId,nextId,index);
        return true;
    }

    @GetMapping("/flush-sort/{parentId}")
    public boolean flushSort(@PathVariable String parentId){
        return resourceService.flushSortByParentId(parentId,true);
    }
}
