package cc.catman.coder.workbench.core.runtime;

import java.util.Optional;

/**
 * 函数变量作用域
 */
public enum EIFunctionVariableScope {
    /**
     * 全局参数,该参数将会被传递给所有的函数,包括在返回后其祖先函数的兄弟函数依然可以访问
     */
    GLOBAL,
    /**
     * 局部参数,该参数只会被传递给当前函数及其子函数,在返回后将会被销毁
     */
    LOCAL,
    /**
     * 临时函数,该参数只会被传递给当前函数,在返回后将会被销毁
     */
    TEMPORARY,

    ;

    public static boolean isGlobal(EIFunctionVariableScope scope) {
        return scope == GLOBAL;
    }

    public static boolean isLocal(EIFunctionVariableScope scope) {
        return scope == LOCAL;
    }

    public static boolean isTemporary(EIFunctionVariableScope scope) {
        return scope == TEMPORARY;
    }

    public static boolean isNotGlobal(EIFunctionVariableScope scope) {
        return scope != GLOBAL;
    }

    public static boolean isNotLocal(EIFunctionVariableScope scope) {
        return scope != LOCAL;
    }

    public static boolean isNotTemporary(EIFunctionVariableScope scope) {
        return scope != TEMPORARY;
    }

    public static boolean isGlobal(String scope) {
        return valueOfOptional(scope).map(EIFunctionVariableScope::isGlobal).orElse(false);
    }

    public static boolean isLocal(String scope) {
        return valueOfOptional(scope).map(EIFunctionVariableScope::isLocal).orElse(false);
    }

    public static boolean isTemporary(String scope) {
        return valueOfOptional(scope).map(EIFunctionVariableScope::isTemporary).orElse(false);
    }


    public static Optional<EIFunctionVariableScope> valueOfOptional(String scope) {
        try {
            return Optional.of(valueOf(scope));
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
