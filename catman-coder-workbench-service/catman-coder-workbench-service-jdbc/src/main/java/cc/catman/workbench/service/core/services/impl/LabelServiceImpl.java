package cc.catman.workbench.service.core.services.impl;

import cc.catman.plugin.core.label.Label;
import cc.catman.plugin.core.label.Labels;
import cc.catman.workbench.service.core.po.base.LabelItemRef;
import cc.catman.workbench.service.core.repossitory.base.ILabelItemRefRepository;
import cc.catman.workbench.service.core.services.ILabelService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LabelServiceImpl implements ILabelService {

    @Resource
    private ILabelItemRefRepository labelItemRefRepository;
    @Override
    public Labels findById(String id) {
        return null;
    }

    @Override
    public Labels findByKindAndBelongID(String kind,  String belongId) {
        // 根据kind和belongId查询标签项
        List<LabelItemRef> labelItems = labelItemRefRepository
                .findAll(
                        Example.of(
                                LabelItemRef.builder().belongId(belongId).kind(kind).build()
                        )
                        , Sort.by(Sort.Direction.ASC, "sorting")
                );
        // 合并标签项
        return convertToLabels(labelItems);
    }

    @Override
    public Labels save(String kind, String belongId, Labels labels) {
        // save操作可以看做是新增和更新的聚合体,所以,先删除,再新增
        Labels oldLabels = deleteByKindAndBelongId(kind, belongId);
        Set<Map.Entry<String, Label>> entries = labels.getItems().entrySet();
        for (Map.Entry<String, Label> entry : entries) {
            AtomicInteger order = new AtomicInteger(0);
            entry.getValue().getValue().forEach(value -> {
                LabelItemRef labelItemRef = LabelItemRef.builder()
                        .belongId(belongId)
                        .kind(kind)
                        .name(entry.getKey())
                        .value(value)
                        .sorting(order.incrementAndGet())
                        .build();
                labelItemRefRepository.save(labelItemRef);
            });
        }
        // 创建快照
        return labels;
    }

    @Override
    public Labels deleteByKindAndBelongId(String kind, String belongId) {
        Labels oldLabels = findByKindAndBelongID(kind, belongId);
        List<LabelItemRef> oldLabelItemRefs = labelItemRefRepository.findAll(
                Example.of(
                        LabelItemRef.builder().belongId(belongId).kind(kind).build()
                )
        );
        if (oldLabels != null) {
            labelItemRefRepository.deleteAll(oldLabelItemRefs);
        }
        return convertToLabels(oldLabelItemRefs);
    }

    @Override
    public List<Labels> findAllByKindAndBelongId(String kind, String belongId) {
        // 拆分条件
        return null;
    }


    protected  Labels convertToLabels(List<LabelItemRef> labelItemRefs){
        Labels labels=Labels.empty();
        labelItemRefs.forEach(labelItemRef -> {
            labels.add(labelItemRef.getName(),labelItemRef.getValue());
        });
        return labels;
    }
}
