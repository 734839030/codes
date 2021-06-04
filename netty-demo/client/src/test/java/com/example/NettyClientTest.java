package com.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class NettyClientTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> f = new CompletableFuture<>();
        f.complete("1");
        String s = f.get();
        System.out.println(s);
    }

    @Test
    void doOpen() throws IOException, InterruptedException {
        NettyClient nettyClient = new NettyClient("127.0.0.1", 9011);
        nettyClient.doOpen();
        nettyClient.doConnect();
        while (true) {
            Thread.sleep(5000);
            nettyClient.send(nettyClient.getChannel(), new CommonProtocol(MessageDispatcher.CMD_HELLO, "hello server".getBytes(StandardCharsets.UTF_8)));
            CommonProtocol send = nettyClient.send(nettyClient.getChannel(), new CommonProtocol(MessageDispatcher.CMD_HELLO, "hello server1".getBytes(StandardCharsets.UTF_8)), 5000);
            System.out.println(new String(send.getBody()));
        }
    }
}