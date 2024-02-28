package cc.catman.workbench.api.server.websocket.message;


import jakarta.websocket.MessageHandler;

/**
 * 当收到任务执行消息时,将调用该处理器
 */
public class ValueProviderExecMessageHandler implements MessageHandler.Whole<String> {

    public void onMessage(String message) {
        // 接收到节点的消息
        // 解析消息,调用调度器,执行任务
        // 在debug模式下,任务的执行过程由websocket消息驱动,在非debug模式下,任务的执行过程由调度器驱动

    }
}
