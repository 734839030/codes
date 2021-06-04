package com.example;


import org.junit.jupiter.api.Test;

import java.io.IOException;

public class NettyServerTest {

    @Test
    void doOpen() throws IOException, InterruptedException {
        NettyServer nettyServer = new NettyServer(9011);
        nettyServer.doOpen();
        System.in.read();
    }

    @Test
    void doClose() {
    }
}