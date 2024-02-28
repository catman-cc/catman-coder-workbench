package cc.catman.coder.workbench.core.node;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class DefaultWorkerManager implements IWorkerManager{

    @Builder.Default
    private Map<String,IWorker> workers=new LinkedHashMap<>();
    @Builder.Default
    private List<IWorkerWatcher> watchers=new ArrayList<>();

    public DefaultWorkerManager(Map<String, IWorker> workers, List<IWorkerWatcher> watchers) {
        this.workers = workers;
        this.watchers = watchers;
    }

    @Override
    public void register(IWorker worker) {
        if (this.workers.containsKey(worker.getId())) {
            return;
        }
        this.workers.put(worker.getId(), worker);
        for (IWorkerWatcher watcher : this.watchers) {
            watcher.onWorkerRegister(worker);
        }
    }

    @Override
    public void unregister(IWorker worker) {
        if (!this.workers.containsKey(worker.getId())) {
            return;
        }
        for (IWorkerWatcher watcher : this.watchers) {
            watcher.onWorkerUnregister(worker);
        }
        this.workers.remove(worker.getId());

    }

    @Override
    public List<IWorker> list() {
        return this.workers.values().stream().toList();
    }

    @Override
    public void watch(IWorkerWatcher watcher) {
        watcher.setWorkerManager(this);
        this.watchers.add(watcher);
    }

    @Override
    public void unwatch(IWorkerWatcher watcher) {
        this.watchers.remove(watcher);
    }
}
