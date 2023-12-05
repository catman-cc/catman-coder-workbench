package cc.catman.workbench.service.core.services.impl;

import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.po.ResourceRef;
import cc.catman.workbench.service.core.repossitory.ResourceRefRepository;
import cc.catman.workbench.service.core.services.IResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Service
public class ResourceServiceImpl implements IResourceService {

    private static String RESOURCE_ROOT_ID = "__root_1";
    private static String RESOURCE_ROOT_NAME = "root";
    private static String RESOURCE_KIND = "resource";

    @javax.annotation.Resource
    private ModelMapper modelMapper;
    @javax.annotation.Resource
    private ResourceRefRepository resourceRefRepository;

    @Override
    public Resource findById(String id, int depth) {
        return resourceRefRepository.findById(id).map(resourceRef -> {
            Resource resource = modelMapper.map(resourceRef, Resource.class);

            // 继续递归,直到无法获取数据
            List<Resource> children = listByParentId(resourceRef.getId(), depth);
            children
                    .stream()
                    .map(child -> {
                                if (child.isLeaf()) {
                                    return child;
                                }
                                child.setChildren(listByParentId(child.getId(), depth));
                                return child;
                            }
                    ).forEach(resource::addChildren);
            return resource;
        }).orElse(null);
    }

    @Override
    public Resource findByKindAndResourceId(String kind, String resourceId) {
        return resourceRefRepository.findOne(Example.of(ResourceRef.builder().kind(kind).resourceId(resourceId).build()))
                .map(resourceRef -> modelMapper.map(resourceRef, Resource.class)).orElse(null);
    }

    @Override
    public List<Resource> listByParentId(String parentId) {
        return resourceRefRepository.findAll(Example.of(ResourceRef.builder().parentId(parentId).build()))
                .stream().map(resourceRef -> modelMapper.map(resourceRef, Resource.class)).toList();
    }

    @Override
    public List<Resource> listByParentId(String parentId, int depth) {
        AtomicInteger depthCounter = new AtomicInteger(depth);
        if (depthCounter.get() == 0) {
            return Collections.emptyList();
        }
        return resourceRefRepository.findAll(Example.of(ResourceRef.builder().parentId(parentId).build()))
                .stream().map(resourceRef -> modelMapper.map(resourceRef, Resource.class))
                .map(resource -> {
                    resource.setChildren(listByParentId(resource.getId(), depthCounter.decrementAndGet()));
                    return resource;
                }).toList();
    }

    @Override
    public Resource findRoot(int depth) {
        Resource root = findById(RESOURCE_ROOT_ID, depth);
        return Optional.ofNullable(root).orElseGet(() -> {
            ResourceRef resourceRef = ResourceRef.builder()
                    .id(RESOURCE_ROOT_ID)
                    .name(RESOURCE_ROOT_NAME)
                    .kind(RESOURCE_KIND)
                    .leaf(false)
                    .build();
            ResourceRef rootResourceRef = resourceRefRepository.save(resourceRef);
            return modelMapper.map(rootResourceRef, Resource.class);
        });
    }

    @Override
    public Resource findRoot() {
        return findRoot(-1);
    }

    @Override
    public Resource save(Resource resource) {
        if (Optional.ofNullable(resource).isEmpty()){
            return null;
        }
        ResourceRef resourceRef = modelMapper.map(resource, ResourceRef.class);
        if (Optional.ofNullable(resourceRef.getParentId()).isEmpty()) {
            resourceRef.setParentId(RESOURCE_ROOT_ID);
        }
        if (Optional.ofNullable(resourceRef.getParentId()).isEmpty()) {
            throw new RuntimeException("parentId can not be null");
        }
        ResourceRef saved = resourceRefRepository.saveAndFlush(resourceRef);
        Resource saveResource = modelMapper.map(saved, Resource.class);
        // 如果资源包含子资源,则需要递归保存子资源
        Optional.ofNullable(resource.getChildren()).ifPresent(children -> {
            saveResource.setChildren(children.stream()
                    .peek(c -> c.setParentId(saved.getParentId()))
                    .map(this::save).toList());
        });
        return saveResource;
    }

    @Override
    public Resource update(Resource resource) {
        return save( resource);
    }

    @Override
    public void deleteById(String id) {
        resourceRefRepository.deleteById(id);
        listByParentId(id, 0).forEach(child -> deleteById(child.getId()));
    }

    @Override
    public void deepWithHandler(String id, Consumer<Resource> handler) {
        Resource res = findById(id, 0);
        deepWithResourceHandler(res, handler);
    }

    public void deepWithResourceHandler(Resource resource, Consumer<Resource> handler){
        Optional.ofNullable(resource).ifPresent(r->{
            listByParentId(r.getId(), 1).forEach(child -> deepWithResourceHandler(child, handler));
            handler.accept(r);
        });
    }
}
