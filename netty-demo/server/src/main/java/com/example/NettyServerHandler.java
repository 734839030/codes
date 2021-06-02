package com.example;

import io.netty.channel.*;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * netty 通用处理
 */
public class NettyServerHandler extends ChannelDuplexHandler {

    private final Map<String, Channel> channels = new ConcurrentHashMap<String, Channel>();

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
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        boolean success = false;
        try {
            ChannelFuture channelFuture = ctx.channel().writeAndFlush(msg);
            success = channelFuture.await(2, TimeUnit.SECONDS);
            Throwable cause = channelFuture.cause();
            if (channelFuture.cause() != null) {
                throw cause;
            }
        } catch (Throwable throwable) {

        }

    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public Map<String, Channel> getChannels() {
        return channels;
    }

    public String toAddressString(InetSocketAddress address) {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }
}
