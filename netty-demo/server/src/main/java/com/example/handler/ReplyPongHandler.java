package com.example.handler;

import com.example.CommonProtocol;
import com.example.MessageDispatcher;
import io.netty.channel.Channel;

public class ReplyPongHandler implements MessageHandler {

    @Override
    public void received(Channel channel, CommonProtocol msg) {
        System.out.println("send pong...");
        send(channel, new CommonProtocol(MessageDispatcher.CMD_PING, null));
    }
}
