package cc.catman.coder.workbench.core.executor;

public enum ExecutorJoinCodeStatus {
    /**
     * 接入码状态
     */
    WAIT_ACTIVE("待激活"),
    VALID("有效"),
    INVALID("无效"),
    EXPIRED("过期"),
    USED("已使用"),
    DISABLED("禁用"),
    ;

    private String desc;

    ExecutorJoinCodeStatus(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
