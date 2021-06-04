package com.example.handler;

import com.example.CommonProtocol;
import io.netty.channel.Channel;

import java.nio.charset.StandardCharsets;

public class HelloResponeHandler implements MessageHandler {

    @Override
    public void received(Channel channel, CommonProtocol msg) {
        System.out.println("received hello response:" + new String(msg.getBody(), StandardCharsets.UTF_8));
        msg.getInvokerId();
    }
}
