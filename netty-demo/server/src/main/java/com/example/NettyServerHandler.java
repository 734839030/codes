package com.example;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty 通用处理
 */
@io.netty.channel.ChannelHandler.Sharable
public class NettyServerHandler extends ChannelDuplexHandler {

    private final Map<String, Channel> channels = new ConcurrentHashMap<String, Channel>();
    private MessageDispatcher messageDispatcher;

    public NettyServerHandler(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Channel channel = ctx.channel();
        channels.put(toAddressString((InetSocketAddress) ctx.channel().remoteAddress()), channel);
        System.out.println("channelActive The connection of " + channel.remoteAddress() + " -> " + channel.localAddress() + " is established.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        channels.remove(toAddressString((InetSocketAddress) ctx.channel().remoteAddress()));
        System.out.println("channelInactive The connection of " + channel.remoteAddress() + " -> " + channel.localAddress() + " is disconnected.");
     //   ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        messageDispatcher.dispatch(ctx.channel(), (CommonProtocol) msg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        // server will close channel when server don't receive any heartbeat from client util timeout.
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            System.out.println(" IdleStateEvent triggered, close channel " + channel);
           /* try {
                channel.close();
            } finally {
                channels.remove(toAddressString((InetSocketAddress) ctx.channel().remoteAddress()));
            }*/
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        System.out.println(" exception Caught:" + cause.getMessage() + ", close channel " + channel);
        if (null == channel) {
            return;
        }
        try {
            channel.close();
        } finally {
            channels.remove(toAddressString((InetSocketAddress) channel.remoteAddress()));
        }

    }

    public Map<String, Channel> getChannels() {
        return channels;
    }

    public String toAddressString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
}
