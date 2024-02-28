package cc.catman.workbench.service.core.services.impl;

import cc.catman.workbench.service.core.repossitory.base.IGroupRepository;
import cc.catman.workbench.service.core.entity.Group;
import cc.catman.workbench.service.core.po.base.GroupRef;
import cc.catman.workbench.service.core.services.IGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.Optional;

@Service
public class GroupServiceImpl implements IGroupService {

    @Resource
    protected ModelMapper modelMapper;

    @Resource
    private IGroupRepository groupRepository;

    @Override
    public Optional<Group> findById(String id) {
        // 从数据库中获取指定的分组数据
        return Optional.ofNullable(id).flatMap(i -> groupRepository.findById(i)
                .map(gp -> {
                    Group g = modelMapper.map(gp, Group.class);
                    // 需要递归处理所属父分组数据
                    Optional.ofNullable(gp.getParent())
                            .flatMap(this::findById)
                            .ifPresent(g::setParent);
                    return g;
                }));
    }

    @Override
    public Group save(Group group) {
        if (group.getParent() != null) {
            group.setParent(save(group.getParent()));
        }

        GroupRef save = groupRepository.save(modelMapper.map(group, GroupRef.class));
        return modelMapper.map(save, Group.class);
    }
}
