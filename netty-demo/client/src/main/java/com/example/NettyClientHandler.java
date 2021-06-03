package com.example;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;

@io.netty.channel.ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<CommonProtocol> {

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
       // ctx.close();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, CommonProtocol msg) throws Exception {
        messageDispatcher.dispatch(ctx.channel(),  msg);
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        // server will close channel when server don't receive any heartbeat from client util timeout.
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            System.out.println("IdleStateEvent triggered, send heartbeat");
          //  ChannelFuture channelFuture = channel.writeAndFlush(new CommonProtocol(MessageDispatcher.CMD_PING, null));
            //channelFuture.awaitUninterruptibly(2000);
            //Throwable cause = channelFuture.cause();
            //System.out.println(cause);
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
