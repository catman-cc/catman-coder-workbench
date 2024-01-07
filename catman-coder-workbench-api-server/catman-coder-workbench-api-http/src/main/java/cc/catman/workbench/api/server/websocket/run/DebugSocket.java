package cc.catman.workbench.api.server.websocket.run;

import cc.catman.workbench.api.server.websocket.run.debug.IDebugSession;
import cc.catman.workbench.api.server.websocket.run.debug.IDebugSessionManager;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * 调试会话的websocket

 * 链接建立后,将会执行一下几个必须的过程:
 * - 初始化ValueProviderDefinition信息,支持两种模式:
 *   1. 传入现有的ValueProviderDefinition ID,此时,将会使用该ValueProviderDefinition ID对应的ValueProviderDefinition来执行任务
 *   2. 传入ValueProviderDefinition的json字符串,此时,将会使用该json字符串来创建ValueProviderDefinition,并使用该ValueProviderDefinition来执行任务
 * - Debug上下文解析顶层上下文的变量信息,并进行最简单的校验,如果校验失败,推送必传参数信息,并等待调试者传入必传参数
 * - 推送断点信息,等待调试者传入断点信息
 * - 推送watchpoint信息,等待调试者传入watchpoint信息
 * - 推送exceptionpoint信息,等待调试者传入exceptionpoint信息
 * - 推送ready状态,等待调试者传入start指令
 * - 接收到start指令后,开始执行任务
 * - 执行任务过程中,如果遇到断点,将会基于当前断点,推送变量等信息,并等待调试者传入继续执行指令
 *   调试者可以在pause状态下,修改变量的值,并传入继续执行指令,此时,将会使用调试者传入的变量值来继续执行任务
 * - 执行任务过程中,如果遇到异常,将会基于当前异常,推送变量,异常等信息,但不会断开连接,并等待调试者传入继续执行指令
 * - 在任务的执行过程中,调试者可以随时返回到某一个断点,修改变量的值,并继续执行任务,但是不保证幂等性,
 *   重复调用,在使用系统外三方服务时,可能会产生副作用,比如,重复操作数据库,重复发送短信等
 *   - 针对数据库,可以考虑在debug下为每一次mysql调用都创建一个事务暂存区,在调试者返回到某一个断点时,将会回滚该事务暂存区
 *     但是该特性需要底层数据库支持,目前mysql不支持该特性
 *
 * - 在新建立一个调试任务时,可以配置是否持久化该调试任务上下文.如果持久化,则可以在调试者断开连接后,重新连接,并继续执行任务
 * - 同时也可以配置是否在调试者断开连接后,经过多长时间后,自动销毁该调试任务上下文
 * - 在调试者断开连接后,如果调试任务上下文被销毁,则调试者将无法继续执行任务,需要重新创建调试任务,该功能需要推送给调试者
 *
 *
 */
@Component
@ServerEndpoint(value = "/ws/debug/{code}")
public class DebugSocket implements ApplicationContextAware {

    public static IDebugSessionManager debugSessionManager;

    @Autowired
    public void setDebugSessionManager(IDebugSessionManager debugSessionManager) {
        DebugSocket.debugSessionManager = debugSessionManager;
    }

    private ApplicationContext applicationContext;

    /**
     * 当一个调试会话接入时,需要将该调试会话的session保存起来,以便后续向该调试会话发送消息
     * @param session 调试会话的session
     * @param code 调试会话的code
     */
    @OnOpen
    @SneakyThrows
    public void onOpen(Session session,@PathParam("code") String code) {
        // 当一个调试会话接入时,需要将该调试会话的session保存起来,以便后续向该调试会话发送消息
        IDebugSession debugSession = debugSessionManager.getOrCreateDebugSession(code,session);
        // 创建调试会话后,需要调用其init方法,初始化调试会话
        debugSession.start();

    }

    @OnMessage
    @SneakyThrows
    public void onMessage(Session session, String message) {
        // 这要求DebugSession内部需要有一个状态机,用于控制调试会话的状态
        session.getBasicRemote().sendText("hello");
    }

    @Override
    public void setApplicationContext(org.springframework.context.ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
