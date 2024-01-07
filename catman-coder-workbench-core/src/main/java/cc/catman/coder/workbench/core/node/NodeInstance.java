package cc.catman.coder.workbench.core.node;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NodeInstance extends Node{


    /**
     * 最后一次ping的时间
     */
    private long lastPingTime;

    /**
     * ping间隔时长,单位毫秒
     */
    private long pingInterval;

    /**
     *  节点启动时间
     */
    private long startTime;


    /**
     * 此次重试次数,当ping失败时,会进行重试,如果重试次数超过一定的次数,则认为节点已经下线
     * 重试次数会在每次ping失败时,进行累加
     * 重试次数会在每次ping成功时,进行清零
     */
    private int retryCount;

    /**
     * 最大重试次数,如果该值操过了Integer.MAX_VALUE,表示其实际运行时间为: Integer.MAX_VALUE * pingInterval
     *   假设pingInterval为1秒,则最大运行时间为: 68年
     * 所以,设计为int类型,足够使用,当然如果修改为long,最大运行时间为: 292471208678年
     */
    private int maxRetryCount;

    /**
     * 稳定性,当节点重试次数超过一定的次数时,会计算节点的稳定性,稳定性越高,则越不容易下线
     * 每一次执行ping操作,都会计算稳定性,计算方式为: 1-( maxRetryCount/((当前时间 - startTime)/pingInterval))
     * 解释计算方式:
     *   1. ((当前时间 - startTime)/pingInterval): 计算当前时间内,应该执行的ping次数
     *   2. maxRetryCount/((当前时间 - startTime)/pingInterval): 计算重试次数和应该执行的ping次数的比值,比值越小,则稳定性越高
     *   3. 1-( maxRetryCount/((当前时间 - startTime)/pingInterval)): 计算稳定性,稳定性越高,则越不容易下线
     */
    private double stabilityRate;

    /**
     * 是否在线
     */
    private boolean isOnline;


    /**
     * 节点的状态
     */
    private NodeStatus status;

    /**
     * 节点的已用内存,单位字节
     * 持有该值的目的是为了调度执行器的时候,可以根据节点的内存使用情况,进行调度
     */
    private long usedMemory;


    INodeService nodeService;
}
