package cc.catman.coder.workbench.core.node;

import cc.catman.coder.workbench.core.message.MessageBus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DefaultWorker implements IWorker{

    private String id;

    private List<String> capabilities;

    private WorkSystemInfo systemInfo;

    private IWorkerManager workerManager;

    private MessageBus messageBus;

    private IExecutorService executorService;

    private boolean local;

    @Override
    public WorkSystemInfo geSystemInfo() {
        return this.systemInfo;
    }

    @Override
    public boolean hasCapability(String capability) {
        return this.capabilities.contains(capability);
    }

}
