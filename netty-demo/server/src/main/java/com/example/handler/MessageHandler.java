package com.example.handler;

import com.example.CommonProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public interface MessageHandler {

    void received(Channel channel, CommonProtocol msg);

    /**
     * 异步，不关心结果
     *
     * @param channel
     * @param msg
     */
    default void send(Channel channel, CommonProtocol msg) {
        this.send(channel, msg, false, 0);
    }

    default void send(Channel channel, CommonProtocol msg, int timeout) {
        this.send(channel, msg, true, timeout);
    }

    default void send(Channel channel, CommonProtocol msg, boolean sync, int timeout) {
        boolean success = false;
        try {
            ChannelFuture channelFuture = channel.writeAndFlush(msg);
            if (sync) {
                success = channelFuture.awaitUninterruptibly(timeout);
            }
            Throwable cause = channelFuture.cause();
            if (null != cause) {
                throw cause;
            }
        } catch (Throwable throwable) {
            throw new RuntimeException("Failed to send message   to " + channel.remoteAddress()
                    + " cause (" + throwable.getMessage() + ") ");
        }
        if (sync && !success) {
            throw new RuntimeException("Failed to send message   to " + channel.remoteAddress()
                    + " in timeout(" + timeout + "ms) limit");
        }
    }
}
