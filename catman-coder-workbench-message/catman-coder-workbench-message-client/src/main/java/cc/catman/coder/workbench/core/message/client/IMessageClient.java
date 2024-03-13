package cc.catman.coder.workbench.core.message.client;

import cc.catman.coder.workbench.core.message.system.CreateChannelOptions;

public interface IMessageClient {
    void create(CreateChannelOptions options, ICreateChannelCallback callback);
}
