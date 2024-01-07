package cc.catman.coder.workbench.core.node;

/**
 * 节点操作者,负责对节点进行操作,非线程安全,和节点一一对应
 */
public interface INodeOperator {

    NodeInstance getNodeInstance();

    void setNodeInstance(NodeInstance nodeInstance);

    /**
     * 向节点发送ping消息,如果节点没有响应,则返回false
     */
    boolean ping();

    void close();


}
