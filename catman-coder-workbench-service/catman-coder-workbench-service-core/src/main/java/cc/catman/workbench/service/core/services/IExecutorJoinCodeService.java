package cc.catman.workbench.service.core.services;

import cc.catman.coder.workbench.core.executor.ExecutorJoinCode;
import cc.catman.coder.workbench.core.executor.ExecutorJoinCodeStatus;
import cc.catman.workbench.service.core.common.page.PageParam;
import cc.catman.workbench.service.core.common.page.VPage;

import java.util.List;
import java.util.Optional;

public interface IExecutorJoinCodeService {

    Optional<ExecutorJoinCode> findById(String id);

    void fuzzy();

    Optional<ExecutorJoinCode> findByJoinCode(String joinCode);

    VPage<ExecutorJoinCode> fuzzy(PageParam page, String keyword);

    VPage<ExecutorJoinCode> fuzzy(PageParam page, String name, String code, String key, List<ExecutorJoinCodeStatus> states);

    boolean invalidJoinCode(String joinCode,String belongId);

    VPage<ExecutorJoinCode> page(PageParam page);

    String createJoinCode();

    ExecutorJoinCode createExecutorJoinCode();

    List<ExecutorJoinCode> findAll(String keyword);

    ExecutorJoinCode save(ExecutorJoinCode executorJoinCode);

    /**
     * 复制接入码实例,但是移除所有状态,包含id信息等数据
     * @param executorJoinCode 接入码实例
     * @return 移除状态的接入码实例
     */
     ExecutorJoinCode copyAndNoState(ExecutorJoinCode executorJoinCode);
    default ExecutorJoinCode flushJoinCode(ExecutorJoinCode executorJoinCode){
        return flushJoinCode(executorJoinCode,false);
    }

    default ExecutorJoinCode flushJoinCode(ExecutorJoinCode executorJoinCode,boolean oldKeepValid){
        return flushJoinCode(executorJoinCode,createJoinCode(),oldKeepValid,"刷新");
    }


    default ExecutorJoinCode onlyFlushJoinCode(String id){
        ExecutorJoinCode joinCode = this.findById(id).orElseThrow(() -> new RuntimeException("接入码不存在"));
        joinCode.setCode(createJoinCode());
        return this.save(joinCode);
    }

    ExecutorJoinCode flushJoinCode(ExecutorJoinCode executorJoinCode,String newCode,boolean oldKeepValid,String invalidReason);

    void deleteById(String id);

    void deleteByJoinCode(String joinCode);
}
