package com.example;

import com.example.handler.MessageHandler;
import com.example.handler.ReplyHelloHandler;
import com.example.handler.ReplyPongHandler;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class MessageDispatcher {

    public static final int CMD_PING = 0;

    static Map<Integer, MessageHandler> handlers = new HashMap<>();

    {
        handlers.put(CMD_PING, new ReplyPongHandler());
        handlers.put(1000, new ReplyHelloHandler());
    }

    public void dispatch(Channel channel, CommonProtocol commonProtocol) {
        MessageHandler messageHandler = handlers.get(commonProtocol.getCmd());
        if (null == messageHandler) {
            throw new RuntimeException("cmd [" + commonProtocol.getCmd() + "] not found");
        }
        messageHandler.received(channel, commonProtocol);
    }

}
