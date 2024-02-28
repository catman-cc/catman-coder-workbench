package cc.catman.workbench.api.server.configuration.exception.ecodes;

import java.io.Serializable;

/**
 * ECode： Exception Code
 * <p>
 * 为了简便，简写为ECode，是系统中异常码的基础定义
 */
public interface ECode<T extends Serializable> {
    /**
     * 获取异常码，通常返回整形即可
     *
     * @return 异常码
     */
    T getCode();

    /**
     * 获取异常信息
     *
     * @return 异常信息
     */
    String getMessage();
}
