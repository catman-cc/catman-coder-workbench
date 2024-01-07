package cc.catman.coder.workbench.core.message;

import java.util.function.Function;

/**
 * 消息连接管理器
 */
public interface MessageConnectionManager {
    /**
     * 获取连接
     * @param id 连接id
     * @return 连接
     */
    MessageConnection<?> getConnection(String id);

    /**
     * 获取或创建连接
     * @param id 连接id
     * @param function 创建连接的函数
     * @return 连接
     */
    MessageConnection<?> getOrCreateConnection(String id, Function<String, MessageConnection<?>> function);

    /**
     * 添加连接
     * @param connection 连接
     */
    void addConnection(MessageConnection<?> connection);

    /**
     * 移除连接
     * @param id 连接id
     */
    void removeConnection(String id);

    /**
     * 移除连接
     * @param connection 连接
     */
    void removeConnection(MessageConnection<?> connection);

}
