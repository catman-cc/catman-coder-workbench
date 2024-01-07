package cc.catman.coder.workbench.core.node;

/**
 * 节点操作接口,所有接入到系统的节点,都需要实现此接口,并提供对应的操作
 * 节点在接入时,可以根据自己的需要提供更上一层的协议,比如http,websocket等
 * 而具体的协议服务,应该分为两部分,一部分是节点的服务,一部分是节点的客户端,在服务端,应该可以无感知的操作节点信息
 */
public interface INodeService {
}
