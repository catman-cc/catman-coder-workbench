package cc.catman.coder.workbench.core.message.transaction;
// 暂时还没有想好是否需要提供一个事物机制,事务机制的最大好处就是在分布式调度时,可以确保完成的一致性.
// 但因为目前没有持久化机制和日志机制,所以想要提供事务机制,需要先完成持久化机制和日志机制.