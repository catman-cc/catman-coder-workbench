package cc.catman.workbench.service.core.services.impl;

import cc.catman.workbench.service.core.entity.Snapshot;
import cc.catman.workbench.service.core.services.ISnapshotService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class SnapshotServiceImpl implements ISnapshotService {
    @Override
    public Optional<Snapshot> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Snapshot> list(String belongId, String kind) {
        return null;
    }

    @Override
    public List<Snapshot> rename(String id, String name) {
        return null;
    }

    @Override
    public Snapshot create(String belongId, String kind, String jsonValue) {
        return null;
    }

    @Override
    public Snapshot create(String belongId, String kind, String name, String jsonValue) {
        return null;
    }

    @Override
    public Snapshot create(String belongId, String kind, String name, Object value) {
        return null;
    }

    @Override
    public Snapshot create(Consumer<Snapshot> creator) {
        return null;
    }

    @Override
    public Snapshot save(Snapshot snapshot) {
        return null;
    }

    @Override
    public List<Snapshot> deleteByBelongId(String belongId, String kind) {
        return null;
    }

    @Override
    public Optional<Snapshot> deleteById(String id) {
        return Optional.empty();
    }
}
