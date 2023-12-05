package cc.catman.workbench.service.core.services;

import cc.catman.workbench.service.core.po.TagRef;

import java.util.List;
import java.util.Optional;

public interface ITagService {

    Optional<TagRef> findById(String id);

    List<TagRef> findByKindAndBelongID(String kind, String belongID);

    /**
     * 保存标签
     * @param kind 标签类型
     * @param belongId 标签所属id
     * @param tagRefs 标签
     * @return 保存后的标签
     */
    List<TagRef> save(String kind, String belongId, List<TagRef> tagRefs);

    /**
     * 根据标签类型和标签所属id删除标签
     * @param kind 标签类型
     * @param belongId 标签所属id
     * @return 删除后的标签
     */
    List<TagRef> deleteByKindAndBelongId(String kind, String belongId);
}
