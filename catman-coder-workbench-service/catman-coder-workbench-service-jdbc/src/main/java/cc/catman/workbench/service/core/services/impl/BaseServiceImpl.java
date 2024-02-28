package cc.catman.workbench.service.core.services.impl;

import cc.catman.coder.workbench.core.JSONMapper;
import cc.catman.plugin.core.label.Label;
import cc.catman.plugin.core.label.Labels;
import cc.catman.workbench.service.core.po.base.BaseRef;
import cc.catman.workbench.service.core.po.base.LabelItemRef;
import cc.catman.workbench.service.core.repossitory.base.IBaseRefRepository;
import cc.catman.workbench.service.core.repossitory.base.ILabelItemRefRepository;
import cc.catman.workbench.service.core.repossitory.base.ITagRefRepository;
import cc.catman.workbench.service.core.po.base.TagRef;
import cc.catman.workbench.service.core.services.IBaseService;
import cc.catman.workbench.service.core.services.ILabelService;
import cc.catman.workbench.service.core.services.ITagService;
import cc.catman.workbench.service.core.entity.Base;
import cc.catman.workbench.service.core.entity.Group;
import cc.catman.workbench.service.core.entity.Scope;
import cc.catman.workbench.service.core.entity.Tag;
import cc.catman.workbench.service.core.services.IGroupService;
import cc.catman.workbench.service.core.services.ISnapshotService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.IdGenerator;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class BaseServiceImpl implements IBaseService {

    @Resource
    private JSONMapper jsonMapper;
    @Resource
    private IGroupService groupService;
    @Resource
    private ILabelService labelService;
    @Resource
    private ITagService tagService;

    @Resource
    private IdGenerator idGenerator;

    @Resource
    private ISnapshotService snapshotService;
    @Resource
    private IBaseRefRepository baseRefRepository;

    @Resource
    private ITagRefRepository tagRefRepository;

    @Resource
    private ILabelItemRefRepository labelItemRefRepository;

    @Override
    public Base findBaseById(String id) {
        return baseRefRepository.findById(id).map(this::convertToBase).orElse(null);
    }

    @Override
    public Base findBaseByBelongId(String id) {
        return baseRefRepository.findOne(Example.of(BaseRef.builder().belongId(id).build()))
                .map(this::convertToBase).orElse(null);
    }

    @Override
    public Base findByKindAndBelongId(String kind, String belongId) {
        return baseRefRepository.findOne(Example.of(BaseRef.builder().kind(kind).belongId(belongId).build()))
                .map(this::convertToBase).orElse(null);
    }

    @Transactional
    @Override
    public Base save(Base base, String kind, String belongId) {
        deleteByKindAndBelongId(kind, belongId).ifPresent(b -> {
            snapshotService.create(base.getId(), kind, jsonMapper.toJson(b));
        });

        String id = Optional.ofNullable(base.getId()).orElseGet(() -> idGenerator.generateId().toString());
        // 直接进行保存操作
        BaseRef baseRef = BaseRef.builder().scope(base.getScope().name())
                .id(id)
                .belongId(belongId)
                .wiki(base.getWiki())
                .kind(kind)
                .build();

        Optional.ofNullable(base.getGroup()).ifPresent(group -> {
            String groupId = Optional.ofNullable(group.getId()).orElseGet(() -> groupService.save(group).getId());
            baseRef.setGroupId(groupId);
        });


        Optional.ofNullable(base.getTags()).ifPresent(tags -> {
            tagService.save(kind, belongId, tags.stream().map(tag -> {
                TagRef tr = TagRef.builder()
                                .id(idGenerator.generateId().toString())
                                .kind(kind)
                                .belongId(id)
                                .tag(tag.getName())
                                .build();
                        return tr;
                    }
            ).toList());
        });

        Optional.ofNullable(base.getLabels()).ifPresent(labels -> {
            labelService.save(kind, belongId,labels);
        });

        baseRefRepository.save(baseRef);
        base.setId(id);
        return base;
    }

    @Override
    public Base updateScopeByKindAndBelongID(String kind, String belongId, String scope) {
        return null;
    }

    @Override
    public Optional<Base> deleteByKindAndBelongId(String kind, String belongId) {
        return baseRefRepository.findOne(Example.of(BaseRef.builder().kind(kind).belongId(belongId).build())).map(baseRef -> {
            Base base = new Base();
            base.setId(baseRef.getId());
            base.setScope(Scope.valueOf(baseRef.getScope()));
            base.setWiki(baseRef.getWiki());
            Optional.ofNullable(baseRef.getGroupId()).ifPresent(gid->{
                groupService.findById(gid).ifPresent(base::setGroup);
            }
                    );

            List<TagRef> tags = tagRefRepository.findAll(Example.of(TagRef.builder().kind(kind).belongId(belongId).build()));
            base.setTags(tags.stream().map(tagRef -> Tag.builder().id(tagRef.getId()).name(tagRef.getTag()).build()).toList());
            tagRefRepository.deleteAll(tags);

            Labels labels = labelService.deleteByKindAndBelongId(kind, belongId);
            base.setLabels(labels);
            baseRefRepository.deleteById(baseRef.getId());
            return base;
        });
    }

    /**
     * 将BaseRef转换为Base
     *
     * @param baseRef 基础数据引用
     * @return 基础数据
     */
    protected Base convertToBase(BaseRef baseRef) {
        Base base = new Base();
        base.setScope(Scope.valueOf(baseRef.getScope()));
        base.setWiki(baseRef.getWiki());

        groupService.findById(baseRef.getGroupId()).ifPresent(base::setGroup);

        tagRefRepository.findAll(Example.of(TagRef.builder()
                        .belongId(baseRef.getId())
                        .kind(baseRef.getKind())
                        .build()))
                .forEach(tagRef -> {
                    Tag tag = new Tag();
                    tag.setId(tagRef.getId());
                    tag.setName(tagRef.getTag());
                    base.getTags().add(tag);
                });

        labelItemRefRepository.findAll(
                        Example.of(LabelItemRef.builder()
                                .belongId(baseRef.getId())
                                .kind(baseRef.getKind())
                                .build())
                )
                .forEach(labelItemRef -> {
                    base.getLabels().add(
                            Label.create(labelItemRef.getName(), labelItemRef.getValue())
                    );
                });
        return base;
    }

    protected Base convertToBase(BaseRef baseRef, Group group, Labels labels, List<Tag> tags) {
        Base base = new Base();
        base.setScope(Scope.valueOf(baseRef.getScope()));
        base.setWiki(baseRef.getWiki());
        base.setLabels(labels);
        base.setGroup(group);
        base.setTags(tags);
        return base;
    }
}
