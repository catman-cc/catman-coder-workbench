package cc.catman.workbench.service.core.services.impl;

import cc.catman.workbench.service.core.po.TagRef;
import cc.catman.workbench.service.core.repossitory.ITagRefRepository;
import cc.catman.workbench.service.core.services.ITagService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.IdGenerator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements ITagService {

    @Resource
    private IdGenerator idGenerator;
    @Resource
    private ITagRefRepository tagRefRepository;

    @Override
    public Optional<TagRef> findById(String id) {
        return tagRefRepository.findById(id);
    }

    @Override
    public List<TagRef> findByKindAndBelongID(String kind, String belongID) {
        return tagRefRepository.findAll(Example.of(TagRef.builder().belongId(belongID).kind(kind).build()));
    }

    @Override
    public List<TagRef> save(String kind, String belongId, List<TagRef> tagRefs) {
        List<TagRef> deletedTags = deleteByKindAndBelongId(kind, belongId);
        return tagRefRepository.saveAll(tagRefs.stream().peek(tagRef -> {
            tagRef.setId(Optional.ofNullable(tagRef.getId()).orElseGet(() -> idGenerator.generateId().toString()));
            tagRef.setKind(kind);
            tagRef.setBelongId(belongId);
        }).toList());
    }

    @Override
    public List<TagRef> deleteByKindAndBelongId(String kind, String belongId) {
        List<TagRef> deletedTags = findByKindAndBelongID(kind, belongId);
        tagRefRepository.deleteAll(deletedTags);
        return deletedTags;
    }
}
