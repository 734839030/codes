package com.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

class NettyClientTest {

    @Test
    void doOpen() throws IOException {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 9000);
        nettyClient.doOpen();
        nettyClient.doConnect();
        nettyClient.getChannel().writeAndFlush(new CommonProtocol(MessageDispatcher.CMD_HELLO, "hello server".getBytes(StandardCharsets.UTF_8)));
        System.in.read();
    }
}