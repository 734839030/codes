package com.example;


import org.junit.jupiter.api.Test;

public class NettyServerTest {

    @Test
    void doOpen() {
        NettyServer nettyServer = new NettyServer(9000);
        nettyServer.doOpen();
    }

    @Test
    void doClose() {
    }
}