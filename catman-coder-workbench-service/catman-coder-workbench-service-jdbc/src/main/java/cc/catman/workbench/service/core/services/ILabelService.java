package cc.catman.workbench.service.core.services;

import cc.catman.plugin.core.label.Labels;

import java.util.List;

public interface ILabelService {

    Labels findById(String id);

    Labels findByKindAndBelongID(String kind,  String belongID);

    /**
     * 保存标签
     * @param kind 标签类型
     * @param belongId 标签所属id
     * @param labels 标签
     * @return 保存后的标签
     */
    Labels save(String kind,String belongId,Labels labels);

    /**
     * 根据标签类型和标签所属id删除标签
     * @param kind 标签类型
     * @param belongId 标签所属id
     * @return 删除后的标签
     */
    Labels deleteByKindAndBelongId(String kind,String belongId);

    List<Labels> findAllByKindAndBelongId(String kind,String belongId);
}
