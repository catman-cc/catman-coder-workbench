package cc.catman.workbench.service.core.services.impl;

import cc.catman.workbench.configuration.id.CatManIdentifierGenerator;
import cc.catman.workbench.service.core.entity.Resource;
import cc.catman.workbench.service.core.po.ResourceRef;
import cc.catman.workbench.service.core.repossitory.ResourceRefRepository;
import cc.catman.workbench.service.core.services.IBaseService;
import cc.catman.workbench.service.core.services.IResourceService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ResourceServiceImpl implements IResourceService {

    @javax.annotation.Resource
    private ModelMapper modelMapper;
    @javax.annotation.Resource
    private EntityManager entityManager;
    @javax.annotation.Resource
    private ResourceRefRepository resourceRefRepository;
    @javax.annotation.Resource
    private IBaseService baseService;

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

            return Optional.ofNullable(baseService.findByKindAndBelongId(Resource.class.getSimpleName(), resource.getId()))
                    .map(base -> base.mergeInto(resource))
                    .orElse(resource);
        }).orElse(null);
    }

    @Override
    public Resource findByKindAndResourceId(String kind, String resourceId) {
        return resourceRefRepository.findOne(Example.of(ResourceRef.builder().kind(kind).resourceId(resourceId).build()))
                .map(resourceRef -> {
                    Resource resource = modelMapper.map(resourceRef, Resource.class);
                    return Optional.ofNullable(baseService.findByKindAndBelongId(Resource.class.getSimpleName(), resource.getId()))
                            .map(base -> base.mergeInto(resource))
                            .orElse(resource);
                }).orElse(null);
    }

    @Override
    public List<Resource> listByParentId(String parentId) {
        return resourceRefRepository.findAll(Example.of(ResourceRef.builder().parentId(parentId).build()))
                .stream().map(resourceRef -> {
                    Resource resource = modelMapper.map(resourceRef, Resource.class);
                    return Optional.ofNullable(baseService.findByKindAndBelongId(Resource.class.getSimpleName(), resource.getId()))
                            .map(base -> base.mergeInto(resource))
                            .orElse(resource);
                })
                .sorted((r1, r2) -> {
                    // 判断两个记录的前后顺序,取决于previousId和nextId
                    // 如果previousId为空,则表示是第一个,则排在前面
                    // 如果nextId为空,则表示是最后一个,则排在后面
                    // 如果previousId和nextId都不为空,则比较两个记录的前后顺序
                    if (r1.getPreviousId() == null) {
                        return -1;
                    }
                    if (r1.getNextId() == null) {
                        return 1;
                    }
                    if (r2.getPreviousId() == null) {
                        return -1;
                    }
                    if (r2.getNextId() == null) {
                        return 1;
                    }
                    if (r1.getPreviousId().equals(r2.getId())) {
                        return 1;
                    }
                    if (r2.getPreviousId().equals(r1.getId())) {
                        return -1;
                    }
                    if (r1.getNextId().equals(r2.getId())) {
                        return -1;
                    }
                    if (r2.getNextId().equals(r1.getId())) {
                        return 1;
                    }
                    return 0;
                })
                .toList();
    }

    public List<ResourceRef> sortResources(List<ResourceRef> resources){
        if (resources.size() <= 1) {
            return resources;
        }
        Map<String,ResourceRef> resourceMap = resources.stream().collect(Collectors.toMap(ResourceRef::getId, Function.identity()));
        // 然后开始根据前后顺序进行排序
        ResourceRef current = resources.stream().filter(r -> r.getPreviousId() == null)
                .findFirst().orElse(null);
        if (current==null){
            flushSortIfNeed(resources);
            return null;
        }
        // 不能直接使用while,有可能出现死循环
        List<ResourceRef> sorted=new ArrayList<>();

        for (int i = 0; i < resources.size(); i++) {
            sorted.add(current);
            if (current.getNextId() == null) {
                break;
            }
            ResourceRef next = resourceMap.get(current.getNextId());
            if (next == null) {
                break;
            }
            current = next;
        }
        // 排序完成后进行一次校验,如果排序后的记录数量和原始记录数量不一致,则表示出现了脏数据,此时需要进行修复操作
        if (sorted.size() != resources.size()) {
            // 此时需要进行修复操作
            // 修复操作的原理是,将所有记录的前后顺序全部清空,然后重新排序
           flushSortIfNeed(resources);
           return null;
        }
        return sorted;
    }

    @Override
    public List<Resource> listByParentId(String parentId, int depth) {
        AtomicInteger depthCounter = new AtomicInteger(depth);
        if (depthCounter.get() == 0) {
            return Collections.emptyList();
        }
        List<ResourceRef> all = resourceRefRepository.findAll(Example.of(ResourceRef.builder().parentId(parentId).build()));
        all=sortResources(all);
        if (all==null){
            return listByParentId(parentId,depth);
        }
        return
               all .stream().map(resourceRef -> {
                    Resource resource = modelMapper.map(resourceRef, Resource.class);
                    return Optional.ofNullable(baseService.findByKindAndBelongId(Resource.class.getSimpleName(), resource.getId()))
                            .map(base -> base.mergeInto(resource))
                            .orElse(resource);
                })
                .map(resource -> {
                    resource.setChildren(listByParentId(resource.getId(), depthCounter.decrementAndGet()));
                    return resource;
                })
                .toList();
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

    @Transactional
    @Override
    public Resource save(Resource resource) {
        if (Optional.ofNullable(resource).isEmpty()) {
            return null;
        }
        if (Optional.ofNullable(resource.getId()).isEmpty()) {
            resource.setId(CatManIdentifierGenerator.generateId());
        }
        ResourceRef resourceRef = modelMapper.map(resource, ResourceRef.class);
        if (Optional.ofNullable(resourceRef.getParentId()).isEmpty()) {
            resourceRef.setParentId(RESOURCE_ROOT_ID);
        }
        if (Optional.ofNullable(resourceRef.getParentId()).isEmpty()) {
            throw new RuntimeException("parentId can not be null");
        }
        if (Optional.ofNullable(resourceRef.getPreviousId()).isEmpty() && Optional.ofNullable(resourceRef.getNextId()).isEmpty()) {
            // 如果前后顺序都为空,则默认将其放在最后
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ResourceRef> query = criteriaBuilder.createQuery(ResourceRef.class);
            Root<ResourceRef> root = query.from(ResourceRef.class);

            query.select(root)
                    .where(criteriaBuilder.and(
                            criteriaBuilder.isNull(root.get("nextId"))
                            , criteriaBuilder.equal(root.get("parentId"), resourceRef.getParentId()))
                    );

            entityManager.createQuery(query)
                    .getResultStream().findFirst().ifPresent((first) -> {
                        // 修改第一个记录的前置id为当前记录的id
                        // 修改当前记录的后置id为第一个记录的id
                        String oldNextId = first.getId();
                        first.setNextId(resourceRef.getId());
                        resourceRefRepository.save(first);
                        resourceRef.setPreviousId(oldNextId);
                    });
        }
        ResourceRef saved = resourceRefRepository.saveAndFlush(resourceRef);


        baseService.save(resource, Resource.class.getSimpleName(), saved.getId());
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
        return save(resource);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        listByParentId(id, 0).forEach(child -> deleteById(child.getId()));
        //  处理树级结构的前后顺序
        resourceRefRepository.findById(id).ifPresent(resourceRef -> {
            Optional.ofNullable(resourceRef.getPreviousId()).ifPresent((previousId) -> {
                ResourceRef previousResource = resourceRefRepository.findById(previousId)
                        .orElseThrow(() -> new RuntimeException("resource not found"));
                previousResource.setNextId(resourceRef.getNextId());
                resourceRefRepository.save(previousResource);
            });
            Optional.ofNullable(resourceRef.getNextId()).ifPresent((nextId) -> {
                ResourceRef nextResource = resourceRefRepository.findById(nextId)
                        .orElseThrow(() -> new RuntimeException("resource not found"));
                nextResource.setPreviousId(resourceRef.getPreviousId());
                resourceRefRepository.save(nextResource);
            });
        });
        resourceRefRepository.deleteById(id);

        baseService.deleteByKindAndBelongId(Resource.class.getSimpleName(), id);
    }

    @Override
    public void deepWithHandler(String id, Consumer<Resource> handler) {
        Resource res = findById(id, 0);
        deepWithResourceHandler(res, handler);
    }


    @Transactional
    @Override
    public void move(String id, String parentId, String previousId, String nextId, Integer index) {
        // 依次安装 previousId nextId index
        log.trace("move resource id:{},parentId:{},previousId:{}", id, parentId, previousId);
        if (Optional.ofNullable(id).filter((i) -> !i.isBlank()).isEmpty()) {
            throw new RuntimeException("id can not be null");
        }

        // 获取旧的父级
        Optional<ResourceRef> moveResourceOpt = resourceRefRepository.findById(id);
        if (moveResourceOpt.isEmpty()) {
            throw new RuntimeException("resource not found");
        }
        ResourceRef moveResource = moveResourceOpt.get();

        // 处理前后顺序,将beforeId等于Id的修改为id对应记录的beforeId
        // 将afterId等于id的修改为id对应记录的afterId
        resourceRefRepository.findAll(Example.of(ResourceRef.builder().previousId(id).build()))
                .forEach(resourceRef -> {
                    resourceRef.setPreviousId(moveResource.getPreviousId());
                    resourceRefRepository.save(resourceRef);
                });

        resourceRefRepository.findAll(Example.of(ResourceRef.builder().nextId(id).build()))
                .forEach(resourceRef -> {
                    resourceRef.setNextId(moveResource.getNextId());
                    resourceRefRepository.save(resourceRef);
                });

        if (Optional.ofNullable(previousId).filter((i) -> !i.isBlank()).map((pre) -> {
            // 有前置id,表示不是第一个
            return tryMoveAfterByPreviousId(id, parentId, pre);
        }).orElse(false)) {
            return;
        }
        if (Optional.ofNullable(nextId).filter((i) -> !i.isBlank()).map((next) -> {
            // 有后置id,表示不是最后一个
            return tryMovePreviousByNextId(id, parentId, next);
        }).orElse(false)) {
            return;
        }
        if (Optional.ofNullable(index).filter((i) -> i >= 0).map((idx) -> {
            // 有index,表示需要移动到指定位置
            if (Optional.ofNullable(parentId).filter((pre) -> !pre.isBlank()).isEmpty()) {
                return false;
            }
            return tryMoveToPositionByIndex(id, parentId, idx);
        }).orElse(false)) {
            return;
        }
        // 如果没有前置id,也没有后置id,也没有index,则表示需要移动到第一个
        if (tryMoveFirst(id, parentId)) {
            return;
        }
        // 如果所有的操作都没有成功,则表示出错了,记录日志,抛出异常
        throw new RuntimeException("move resource failed");
    }

    public void deepWithResourceHandler(Resource resource, Consumer<Resource> handler) {
        Optional.ofNullable(resource).ifPresent(r -> {
            listByParentId(r.getId(), 1).forEach(child -> deepWithResourceHandler(child, handler));
            handler.accept(r);
        });
    }

    /**
     * 尝试将记录插入到指定记录的后面,该操作最多影响两条记录
     * 1. previousId对应的记录的nextId修改为当前记录的id
     * 2. nextId对应的记录的previousId修改为当前记录的id
     *
     * @param id         要移动的记录id
     * @param parentId   要移动的记录的父级id
     * @param previousId 要移动的记录的前置id
     * @return 是否移动成功
     */
    protected boolean tryMoveAfterByPreviousId(String id, String parentId, String previousId) {
        if (Optional.ofNullable(previousId).filter((pre) -> !pre.isBlank()).isEmpty()) {
            return false;
        }
        // 根据前置id获取前置记录
        return resourceRefRepository.findById(previousId)
                .map(pre -> {
                    ResourceRef mainResourceRef = resourceRefRepository.findById(id).orElseThrow(() -> new RuntimeException("resource not found"));
                    // 修改nextId影响的记录
                    Optional.ofNullable(pre.getNextId()).filter((next) -> !next.isBlank()).ifPresent((next) -> {
                        // 如果前置记录的后置id不为空,则需要修改后置记录的前置id为当前记录的id
                        ResourceRef nextResource = resourceRefRepository.findById(next)
                                .orElseThrow(() -> new RuntimeException("resource not found"));
                        mainResourceRef.setNextId(next);
                        nextResource.setPreviousId(id);
                        resourceRefRepository.save(nextResource);
                    });
                    // 修改previousId影响的记录
                    pre.setNextId(id);
                    resourceRefRepository.save(pre);
                    // 修改当前记录
                    mainResourceRef.setPreviousId(previousId);
                    mainResourceRef.setParentId(pre.getParentId());
                    // 注意此处并为修改pid
                    // 保存
                    resourceRefRepository.save(mainResourceRef);
                    return true;
                })
                .orElse(false);
    }

    /**
     * 尝试将记录插入到指定记录的前面,该操作最多影响两条记录
     * 1. nextId对应的记录的previousId修改为当前记录的id
     * 2. previousId对应的记录的nextId修改为当前记录的id
     *
     * @param id       要移动的记录id
     * @param parentId 目标父级id
     * @param nextId   要移动的记录的后置id
     * @return 是否移动成功
     */
    protected boolean tryMovePreviousByNextId(String id, String parentId, String nextId) {
        if (Optional.ofNullable(nextId).filter((next) -> !next.isBlank()).isEmpty()) {
            return false;
        }
        // 根据后置id获取后置记录
        // 根据前置id获取前置记录
        return resourceRefRepository.findById(nextId)
                .map(next -> {
                    ResourceRef mainResourceRef = resourceRefRepository.findById(id).orElseThrow(() -> new RuntimeException("resource not found"));
                    // 修改nextId影响的记录
                    Optional.ofNullable(next.getPreviousId()).filter((pre) -> !pre.isBlank()).ifPresent((pre) -> {
                        // 如果前置记录的后置id不为空,则需要修改后置记录的前置id为当前记录的id
                        ResourceRef preResource = resourceRefRepository.findById(pre)
                                .orElseThrow(() -> new RuntimeException("resource not found"));
                        preResource.setNextId(id);
                        resourceRefRepository.save(preResource);
                    });
                    mainResourceRef.setPreviousId(next.getPreviousId());
                    // 修改previousId影响的记录
                    next.setPreviousId(id);
                    resourceRefRepository.save(next);
                    // 修改当前记录
                    mainResourceRef.setNextId(nextId);
                    mainResourceRef.setParentId(next.getParentId());
                    // 注意此处并为修改pid
                    // 保存
                    resourceRefRepository.save(mainResourceRef);
                    return true;
                })
                .orElse(false);
    }

    /**
     * 尝试将记录移动到指定位置,其本质上是将记录移动到指定记录的前面,
     * 该操作最多影响三条记录
     *
     * @param id       要移动的记录id
     * @param parentId 目标父级id
     * @param index    目标位置
     * @return 是否移动成功
     */
    protected boolean tryMoveToPositionByIndex(String id, String parentId, Integer index) {
        if (Optional.ofNullable(index).filter(i -> i >= 0).isEmpty()) {
            return false;
        }
        if (Optional.ofNullable(parentId).filter((pre) -> !pre.isBlank()).isEmpty()) {
            return false;
        }
        // 获取父级下的所有记录
        List<ResourceRef> resourceRefs = resourceRefRepository.findAll(Example.of(ResourceRef.builder().parentId(parentId).build()));
        resourceRefs=sortResources(resourceRefs);
        if (resourceRefs==null){
            resourceRefs=sortResources(resourceRefRepository.findAll(Example.of(ResourceRef.builder().parentId(parentId).build())));
        }
        if (resourceRefs==null){
            throw new RuntimeException("can not re-sort resource");
        }
        if (resourceRefs.size() == 0) {
            // 此处需要注意,可能不存在兄弟节点,此时需要将当前记录的前后顺序修改为null
            ResourceRef mainResourceRef = resourceRefRepository.findById(id).orElseThrow(() -> new RuntimeException("resource not found"));
            mainResourceRef.setPreviousId(null);
            mainResourceRef.setNextId(null);
            resourceRefRepository.save(mainResourceRef);
            return true;
        }
        index = index % resourceRefs.size();
        // 获取目标记录
        ResourceRef targetResourceRef = resourceRefs.get(index);
        // 此时按照byNextId的方式进行移动
        return tryMovePreviousByNextId(id, parentId, targetResourceRef.getId());
    }

    protected boolean tryMoveFirst(String id, String parentId) {
        if (Optional.ofNullable(parentId).filter((pre) -> !pre.isBlank()).isEmpty()) {
            return false;
        }
        // 获取父级下的所有记录
        List<ResourceRef> resourceRefs = resourceRefRepository.findAll(Example.of(ResourceRef.builder().parentId(parentId).build()));
        if (resourceRefs.size() == 0) {
            // 此处需要注意,可能不存在兄弟节点,此时需要将当前记录的前后顺序修改为null
            ResourceRef mainResourceRef = resourceRefRepository.findById(id).orElseThrow(() -> new RuntimeException("resource not found"));
            mainResourceRef.setPreviousId(null);
            mainResourceRef.setNextId(null);
            resourceRefRepository.save(mainResourceRef);
            return true;
        }
        // 如果有必要,在此处刷新前后顺序
        resourceRefs = flushSortIfNeed(resourceRefs);
        // 获取目标记录
        ResourceRef targetResourceRef = resourceRefs.get(0);
        // 此时按照byNextId的方式进行移动
        return tryMovePreviousByNextId(id, parentId, targetResourceRef.getId());
    }

    /**
     * 如果子记录中出现了下面几种情况,则需要刷新子记录的前后顺序
     * - 同时出现了两个及以上的previousId为null的记录
     * - 同时出现了两个及以上的nextId为null的记录
     * - 记录大于1个,但是记录出现了previousId为null和nextId为null的记录
     *
     * @param children 子记录
     */
    protected List<ResourceRef> flushSortIfNeed(List<ResourceRef> children) {
        if (children.size()==0){
            return children;
        }
        if (children.size() == 1) {
            // 如果只有一个记录,则直接将其前后顺序修改为null
            ResourceRef resourceRef = children.get(0);
            if (resourceRef.getPreviousId() != null || resourceRef.getNextId() != null) {
                resourceRef.setPreviousId(null);
                resourceRef.setNextId(null);
                children.set(0, resourceRefRepository.save(resourceRef));
            }
            return children;
        }
        // 如果不存在previousId为null或者nextId为null的记录
        if (children.stream().noneMatch((child) -> child.getPreviousId() == null)) {
            return  this.flushAllChildren(children);
        }
        if (children.stream().noneMatch((child) -> child.getNextId() == null)) {
            return this.flushAllChildren(children);
        }
        // 如果同时有多条记录的previousId或者nextId指向了同一个值
        Map<String,Integer> preCounter=new HashMap<>();
        Map<String,Integer> nextCounter=new HashMap<>();
        children.forEach(child->{
            preCounter.put(child.getPreviousId(),preCounter.getOrDefault(child.getPreviousId(),0)+1);
            nextCounter.put(child.getNextId(),nextCounter.getOrDefault(child.getNextId(),0)+1);
        });
        if (preCounter.values().stream().anyMatch((count) -> count > 1)){
            return this.flushAllChildren(children);
        }
        if (nextCounter.values().stream().anyMatch((count) -> count > 1)){
            return this.flushAllChildren(children);
        }
        // 拥有超过两条记录
        if (children.stream().filter((child) -> child.getPreviousId() == null).count() > 2) {
            // 同时出现了两个及以上的previousId为null的记录
            // 此时需要将所有记录的前后顺序刷新
            return this.flushAllChildren(children);
        }
        if (children.stream().filter((child) -> child.getNextId() == null).count() > 2) {
            // 同时出现了两个及以上的nextId为null的记录
            // 此时需要将所有记录的前后顺序刷新
            return this.flushAllChildren(children);
        }
        return children;
    }

    /**
     * 刷新指定父级下的所有记录的前后顺序
     *
     * @param parentId 父级id
     * @return 是否刷新成功
     */
    public boolean flushSortByParentId(String parentId, boolean deep) {
        if (Optional.ofNullable(parentId).filter((pre) -> !pre.isBlank()).isEmpty()) {
            return false;
        }
        // 获取父级下的所有记录
        List<Resource> children = listByParentId(parentId);
        if (children.size() == 0) {
            return false;
        }
        if (deep) {
            children.forEach(c -> {
                flushSortByParentId(c.getId(), deep);
            });
        }
        this.flushSortIfNeed(children.stream().map((child) -> modelMapper.map(child, ResourceRef.class)).toList());
        return true;
    }

    protected List<ResourceRef> flushAllChildren(List<ResourceRef> children) {
        if (children.size() == 0) {
            return children;
        }
        // 此时需要尝试修改所有记录的前后顺序,此时的顺序是按照index的顺序
        // 依次修改每个记录的前后顺序
        for (int i = 0; i < children.size(); i++) {
            ResourceRef resource = children.get(i);
            if (i == 0) {
                // 如果是第一个,则需要将previousId修改为null
                resource.setPreviousId(null);
            } else {
                // 如果不是第一个,则需要将previousId修改为前一个记录的id
                resource.setPreviousId(children.get(i - 1).getId());
            }
            if (i == children.size() - 1) {
                // 如果是最后一个,则需要将nextId修改为null
                resource.setNextId(null);
            } else {
                // 如果不是最后一个,则需要将nextId修改为后一个记录的id
                resource.setNextId(children.get(i + 1).getId());
            }
        }
        // 保存
        return resourceRefRepository.saveAll(children);
    }
}
