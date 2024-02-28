package cc.catman.coder.workbench.core.executor;

import cc.catman.coder.workbench.core.label.ISelector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DefaultExecutorManager implements IExecutorManager {

    /**
     * 执行器集合,key为id
     */
    private Map<String,IExecutor> executors=new LinkedHashMap<>();

    @Override
    public List<IExecutor> listExecutors() {
        return this.executors.values().stream().toList();
    }

    @Override
    public IExecutor getExecutor(String id) {
        return this.executors.get(id);
    }

    @Override
    public List<IExecutor> find(ISelector selector) {
//        return this.executors.values().stream().filter(executor -> selector.valid(executor.getInformation(),null)).toList();
        return null;
    }

    @Override
    public IExecutorManager addExecutor(IExecutor executor) {
        validateExecutor(executor);

        if (this.executors.containsKey(executor.getId())){
            // 执行器id已存在,目前没有想到需要替换的业务场景
            throw new IllegalArgumentException("executor id already exists");
        }
        executor.start();
        this.executors.put(executor.getId(),executor);
        return this;
    }

    @Override
    public IExecutorManager removeExecutor(String id) {
        IExecutor executor=this.executors.get(id);
        executor.stop();
        return this;
    }

    protected void validateExecutor(IExecutor executor){
        if (executor==null){
            throw new IllegalArgumentException("executor is null");
        }

        if (executor.getId()==null){
            throw new IllegalArgumentException("executor id is null");
        }

//        if (executor.getInformation()==null){
//            throw new IllegalArgumentException("executor information is null");
//        }
//
//        if (executor.getInformation().getId()==null){
//            throw new IllegalArgumentException("executor id is null");
//        }
    }
}
