package cc.catman.workbench.service.core.services;

import cc.catman.workbench.service.core.entity.Snapshot;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface ISnapshotService {
    /**
     * 获取指定的快照信息
     * @param id 快照id
     * @return 快照信息
     */
    Optional<Snapshot> findById(String id);

    /**
     * 获取指定资源的所有快照信息
     * @param belongId 资源ID,可以为空,当为空时,表示获取指定kind的所有快照数据
     * @param kind 资源类型
     * @return 快照信息集合
     */
    List<Snapshot> list(@Nullable String belongId,String kind);

    /**
     * 重命名指定的快照信息
     * @param id 快照唯一标志
     * @param name 新名称,可以为空
     * @return 重命名后的快照信息
     */
    List<Snapshot> rename(String id,@Nullable String name);

    /**
     * 创建快照信息
     * @param belongId  快照所属资源唯一标志
     * @param kind 快照所属资源类型
     * @param name 快照名称
     * @param jsonValue 快照数据
     * @return 快照信息
     */
    Snapshot create(String belongId,String kind,String jsonValue);



    /**
     * 创建快照信息
     * @param belongId  快照所属资源唯一标志
     * @param kind 快照所属资源类型
     * @param name 快照名称
     * @param jsonValue 快照数据
     * @return 快照信息
     */
    Snapshot create(String belongId,String kind,String name,String jsonValue);

    /**
     * 创建快照信息
     * @param belongId  快照所属资源唯一标志
     * @param kind 快照所属资源类型
     * @param name 快照名称
     * @param value 快照数据,将会被转换为json格式进行保存
     * @return 快照信息
     */
    Snapshot create(String belongId,String kind,String name,Object value);

    /**
     * 创建快照信息
     * @param creator 快照信息创建者
     * @return 快照信息
     */
    Snapshot create(Consumer<Snapshot> creator);
    /**
     * 新增快照信息,如果一个没有设置{@link Snapshot#getVersion()}的值,将会根据现有快照信息,自动填充
     * @param snapshot 快照信息
     * @return 快照信息
     */
    Snapshot save(Snapshot snapshot);

    /**
     * 删除指定资源的快照信息
     * @param belongId 所属资源唯一标志
     * @param kind  所属资源类型
     * @return 被删除的快照信息
     */
    List<Snapshot> deleteByBelongId(String belongId,String kind);

    /**
     * 根据快照id移除快照信息
     * @param id 快照id
     * @return 被移除的快照信息
     */
    Optional<Snapshot> deleteById(String id);
}
