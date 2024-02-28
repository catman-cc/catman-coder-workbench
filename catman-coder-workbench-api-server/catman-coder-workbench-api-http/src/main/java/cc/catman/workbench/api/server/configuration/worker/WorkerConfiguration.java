package cc.catman.workbench.api.server.configuration.worker;

import cc.catman.coder.workbench.core.executor.DefaultExecutorManager;
import cc.catman.coder.workbench.core.executor.IExecutorManager;
import cc.catman.coder.workbench.core.node.*;
import cc.catman.coder.workbench.core.schedule.DefaultSchedule;
import cc.catman.coder.workbench.core.schedule.ISchedule;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

//@Configuration
public class WorkerConfiguration {

    @Bean
    public IExecutorManager executorManager(){
        // 初始化执行器管理器
        return new DefaultExecutorManager();
    }
    @Bean
    public IWorkerManager workerManager(){
        // 初始化节点管理器
        DefaultWorkerManager workManager = DefaultWorkerManager.builder().build();
        // 注册执行器监听器
        workManager.watch(new ExecutorWorkWatcher(executorManager()));
        return workManager;
    }
    @Bean
    public ISchedule schedule(){
        // 初始化调度器
        return new DefaultSchedule(executorManager());
    }

    /**
     * 启动本地工作节点
     */
    @PostConstruct
    public void startLocalWorker(){
        DefaultWorker worker = DefaultWorker.builder()
                .id("local" + UUID.randomUUID())
                .local(true)
                .executorService(new LocalExecutorService())
                .messageBus(null)
                .workerManager(workerManager())
                .systemInfo(WorkInfoHelper.getWorkInfo())
                .build();
        workerManager().register(worker);
    }
}
