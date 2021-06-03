package com.example;

import com.example.handler.HelloResponeHandler;
import com.example.handler.MessageHandler;
import com.example.handler.PingResponeHandler;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class MessageDispatcher {

    public static final int CMD_PING = 0;
    public static final int CMD_HELLO = 1000;
    static Map<Integer, MessageHandler> handlers = new HashMap<>();

    {
        handlers.put(CMD_PING, new PingResponeHandler());
        handlers.put(CMD_HELLO, new HelloResponeHandler());
    }

    public void dispatch(Channel channel, CommonProtocol commonProtocol) {
        MessageHandler messageHandler = handlers.get(commonProtocol.getCmd());
        if (null == messageHandler) {
            throw new RuntimeException("cmd [" + commonProtocol.getCmd() + "] not found");
        }
        messageHandler.received(channel, commonProtocol);
    }
}
