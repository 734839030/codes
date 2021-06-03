package com.example;

import io.netty.channel.ChannelFuture;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class NettyClientTest {

    @Test
    void doOpen() throws IOException, InterruptedException {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 9000);
        nettyClient.doOpen();
        nettyClient.doConnect();
        while (true) {
            Thread.sleep(5000);
            ChannelFuture channelFuture = nettyClient.getChannel().writeAndFlush(new CommonProtocol(MessageDispatcher.CMD_HELLO, "hello server".getBytes(StandardCharsets.UTF_8)));
             channelFuture = channelFuture.awaitUninterruptibly();

            System.out.println("success:" + channelFuture.isSuccess());
            System.out.println("cause:" + channelFuture.cause());
        }
    }
}