package cc.catman.workbench.api.server.configuration.join;

import cc.catman.coder.workbench.core.common.IPStrategy;
import cc.catman.coder.workbench.core.executor.ExecutorJoinCode;
import cc.catman.coder.workbench.core.executor.ExecutorJoinCodeStatus;
import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.MessageConnection;
import cc.catman.coder.workbench.core.message.MessageConnectionManager;
import cc.catman.workbench.api.server.configuration.message.connection.WebSocketSessionConnection;
import cc.catman.workbench.service.core.services.IExecutorJoinCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 执行器接入处理器的websocket实现,预置对于的还有其他协议实现,比如基于netty的socket实现
 */
@Component
public class ExecutorJoinWebSocketHandler extends AbstractWebSocketHandler {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private IExecutorJoinCodeService executorJoinCodeService;

    @Resource
    private MessageConnectionManager messageConnectionManager;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 分为几个大类,比如,接入,心跳,指令,日志等
        // 除了接入类和心跳类,其他的都需要等待接入码验证通过后才能处理
        // 接入类处理,获取节点更多信息,比如,操作系统,cpu,内存,磁盘等
        // 这里会进一步进行验证,验证通过后,将执行器的状态设置为在线,如果验证失败,则回传错误信息,并关闭连接

        // 心跳类处理
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        session.sendMessage(new PongMessage());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // javascript WebSocket客户端不支持自定义请求头,所以这里使用url参数来传递接入码
        Map<String, String> pathParameters = new AntPathMatcher()
                .extractUriTemplateVariables("/ws/join/{joinCode}", session.getUri().getPath());
        if (pathParameters.size()!=1){
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        // 读取接入码
        String joinCode = pathParameters.get("joinCode");
        // 获取接入码对应的配置信息,首先是判断在当前链接的实例中是否存在已经使用了该接入码的链接,如果存在,则根据接入码的重复接入策略进行处理E
        Optional<ExecutorJoinCode> joinCodeOpt = executorJoinCodeService.findByJoinCode(joinCode);
        if (joinCodeOpt.isEmpty()){
            session.sendMessage(new TextMessage("接入码不存在,将断开连接"));
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        // 验证接入码的状态
        ExecutorJoinCode executorJoinCode = joinCodeOpt.get();
        ExecutorJoinCodeStatus status=Optional.ofNullable(executorJoinCode.getStatus()).orElse(ExecutorJoinCodeStatus.INVALID);
        if (status!=ExecutorJoinCodeStatus.VALID){
            session.sendMessage(new TextMessage("接入码状态异常,将断开连接,状态:"+status.getDesc()));
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        // 获取接入ip
        String remoteAddress = Optional.ofNullable(session.getRemoteAddress())
                        .map(address -> address.getAddress().getHostAddress())
                        .orElse(null);
        if (remoteAddress==null){
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        // 验证ip
        List<IPStrategy> ipFilter = executorJoinCode.getIpFilter();
        if (ipFilter!=null && !ipFilter.isEmpty()){
            boolean match = ipFilter.stream().anyMatch(ipStrategy -> ipStrategy.canAccess(remoteAddress));
            if (!match){
                session.sendMessage(new TextMessage("接入ip不在允许范围内,将断开连接,ip:"+remoteAddress));
                session.close(CloseStatus.BAD_DATA);
                return;
            }
        }
        // 此时完成初步接入操作
        // 回传消息,验证接入码是否有效
        session.sendMessage(new TextMessage("接入成功,接入码:"+joinCode+",接入ip:"+remoteAddress+",接入时间:"+System.currentTimeMillis()));
        // 将消息通道注册到消息通道管理器中
        MessageConnection<?> connection = messageConnectionManager.getOrCreateConnection(session.getId(), id -> WebSocketSessionConnection.builder()
                .id(id)
                .objectMapper(objectMapper)
                .rawConnection(session)
                .build());
        connection.send(Message.create("init-stage","01"));
        // 此时,接入码完成初步接入操作,接下来需要响应调度器的指令,更新执行器的状态
        session.sendMessage(new PongMessage());
    }
}
