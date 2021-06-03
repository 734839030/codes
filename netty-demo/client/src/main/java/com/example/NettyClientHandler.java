package com.example;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

@io.netty.channel.ChannelHandler.Sharable
public class NettyClientHandler extends ChannelDuplexHandler {

    private MessageDispatcher messageDispatcher;

    public NettyClientHandler(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
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
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        messageDispatcher.dispatch(ctx.channel(), (CommonProtocol) msg);
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
