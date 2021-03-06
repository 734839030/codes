package com.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * server 实现
 */
public class NettyServer {

    // 最好大于心跳时间的2倍
    private int idleTimeout = 60 * 1000;
    private int port = 8080;
    /**
     * netty server bootstrap.
     */
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Map<String, Channel> channels;
    private Channel channel;

    public NettyServer(int port) {
        this.port = port;
    }

    public void doOpen() throws InterruptedException {
        bootstrap = new ServerBootstrap();
        // 如果 bootstrap 监听多个端口，boss线程数写对应个数，这里不是瓶颈所以一个端口一个acceptor线程
        bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
        // 默认cpu 个数 网路通信IO等待较多，线程数量可以适当多点
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new DefaultThreadFactory("NettyServerWorker", true));
        MessageDispatcher messageDispatcher = new MessageDispatcher();
        final NettyServerHandler nettyServerHandler = new NettyServerHandler(messageDispatcher);
        channels = nettyServerHandler.getChannels();
        //  netty default option see DefaultChannelConfig
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)

                .childHandler(new ChannelInitializer<SocketChannel>() {

                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("decoder", new MessageDecoder())
                                .addLast("encoder", new MessageEncoder())
                                .addLast("server-idle-handler", new IdleStateHandler(0, 0, idleTimeout, MILLISECONDS))
                                .addLast("handler", nettyServerHandler);
                    }
                });
        // bind
        ChannelFuture channelFuture = bootstrap.bind(port).syncUninterruptibly();
        System.out.println("server started");
        channel = channelFuture.awaitUninterruptibly().channel();
        // 需要阻塞的可以阻塞
        channel.closeFuture().sync();
    }


    public Collection<Channel> getChannels() {
        return null != channels ? channels.values() : Collections.<Channel>emptyList();
    }


    public void doClose() {
        if (channel != null) {
            channel.close();
        }
        try {
            Collection<Channel> channels = getChannels();
            if (!channels.isEmpty()) {
                for (Channel channel : channels) {
                    try {
                        channel.close();
                    } catch (Throwable e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        try {
            if (bootstrap != null) {
                bossGroup.shutdownGracefully().syncUninterruptibly();
                workerGroup.shutdownGracefully().syncUninterruptibly();
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
        try {
            if (channels != null) {
                channels.clear();
            }
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
    }
}
