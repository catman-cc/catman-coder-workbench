package cc.catman.coder.workbench.core.node;


public class ScheduleWorkerWatcher implements IWorkerWatcher{
    private IWorkerManager workerManager;

    @Override
    public IWorkerManager getWorkerManager() {
        return this.workerManager;
    }

    @Override
    public void setWorkerManager(IWorkerManager workerManager) {
        this.workerManager = workerManager;
    }

    @Override
    public void onWorkerRegister(IWorker worker) {

    }
}
