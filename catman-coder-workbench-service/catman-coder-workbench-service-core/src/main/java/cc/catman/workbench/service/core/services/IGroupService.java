package cc.catman.workbench.service.core.services;


import cc.catman.workbench.service.core.entity.Group;

import java.util.Optional;

/**
 * 分组服务
 */
public interface IGroupService {

    /**
     * 根据id获取分组数据,如果分组包含父级分组数据({@link Group#getParent()}),需要在返回时,递归查询父级分组数据
     * @param id 分组id
     * @return 分组数据
     */
   Optional<Group> findById(String id);

    /**
     * 保存分组数据
     * @param group 分组数据
     * @return 保存后的分组数据
     */
   Group save(Group group);

}
