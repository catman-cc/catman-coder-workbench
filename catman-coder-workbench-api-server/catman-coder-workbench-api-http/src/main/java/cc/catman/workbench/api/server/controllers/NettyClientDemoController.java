package cc.catman.workbench.api.server.controllers;

import cc.catman.coder.workbench.core.message.Message;
import cc.catman.coder.workbench.core.message.netty.NettyMessageClient;
import cc.catman.coder.workbench.core.message.netty.client.NettyClientConfiguration;
import cc.catman.coder.workbench.core.message.netty.serialize.MessageSerializeConfiguration;
import cc.catman.workbench.api.server.configuration.message.serialize.FuryMessageSerialize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/netty")
public class NettyClientDemoController {

    @GetMapping("/send/{msg}")
    public boolean send(@PathVariable(name = "msg") String msg){
        NettyClientConfiguration conf = new NettyClientConfiguration();
        MessageSerializeConfiguration sc = conf.getMessageSerializeConfiguration();
        sc.setSerializeType(FuryMessageSerialize.FURY_SERIALIZE_TYPE);
        sc.getMessageSerializeFactory().register(FuryMessageSerialize.FURY_SERIALIZE_TYPE,new FuryMessageSerialize());

        NettyMessageClient client=NettyMessageClient.create(conf);
        client.connect().writeAndFlush(Message.of(msg));
        return true;
    }
}
