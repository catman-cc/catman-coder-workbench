package cc.catman.workbench.service.core.entity;

/**
 * 可用范围
 */
public enum Scope {
    PUBLIC,
    PRIVATE,

    ;
    cc.catman.coder.workbench.core.common.Scope convert(){
        return cc.catman.coder.workbench.core.common.Scope.valueOf(this.name());
    }
}
