package com.example.handler;

import com.example.CommonProtocol;
import io.netty.channel.Channel;

import java.nio.charset.StandardCharsets;

public class ReplyHelloHandler implements MessageHandler {

    @Override
    public void received(Channel channel, CommonProtocol msg) {
        System.out.println("received:" + new String(msg.getBody(), StandardCharsets.UTF_8));
        msg.setBody("hello world".getBytes(StandardCharsets.UTF_8));
        send(channel, msg);
    }
}
