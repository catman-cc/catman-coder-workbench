package cc.catman.workbench.api.server.configuration.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    private static volatile ThreadPool instance;
    private ThreadPool() {
    }
    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadPool.class) {
                if (instance == null) {
                   synchronized (ThreadPool.class) {
                       instance = new ThreadPool();
                   }
                }
            }
        }
        return instance;
    }
   public  ExecutorService executorService = Executors.newFixedThreadPool(10);

}
