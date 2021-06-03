package com.example.handler;

import com.example.CommonProtocol;
import io.netty.channel.Channel;

public class PingResponeHandler implements MessageHandler {

    @Override
    public void received(Channel channel, CommonProtocol msg) {
        System.out.println("received ping respone");
    }
}
