package com.example;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@io.netty.channel.ChannelHandler.Sharable
public class NettyClientHandler extends ChannelDuplexHandler {

    public static final AttributeKey<Map<Integer, CompletableFuture<CommonProtocol>>> attributeKey = AttributeKey.valueOf("future");
    private MessageDispatcher messageDispatcher;

    public NettyClientHandler(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        ctx.channel().attr(attributeKey).set(new ConcurrentHashMap<>());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Channel channel = ctx.channel();
        System.out.println("channelActive The connection of " + channel.localAddress() + " -> " + channel.remoteAddress() + " is established.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        System.out.println("channelInactive The connection of " + channel.localAddress() + " -> " + channel.remoteAddress() + " is disconnected.");
        // ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CommonProtocol commonProtocol = (CommonProtocol) msg;
        Map<Integer, CompletableFuture<CommonProtocol>> integerCompletableFutureMap = ctx.channel().attr(attributeKey).get();
        CompletableFuture<CommonProtocol> completableFuture = integerCompletableFutureMap.get(commonProtocol.getInvokerId());
        if (null == completableFuture) {
            messageDispatcher.dispatch(ctx.channel(), commonProtocol);
        } else {
            completableFuture.complete(commonProtocol);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        // server will close channel when server don't receive any heartbeat from client util timeout.
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            System.out.println("IdleStateEvent triggered, send heartbeat");
            channel.writeAndFlush(new CommonProtocol(MessageDispatcher.CMD_PING, null));
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
        channel.close();
    }
}
