package com.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class NettyClient {

    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;

    private volatile Channel channel;
    private int timeout = 2000;
    private int heartbeatInterval = 20 * 1000;
    private String ip;
    private int port;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void doOpen() {
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2, new DefaultThreadFactory("NettyServerWorker", true));
        bootstrap.group(workerGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .channel(NioSocketChannel.class);

        MessageDispatcher messageDispatcher = new MessageDispatcher();
        NettyClientHandler nettyClientHandler = new NettyClientHandler(messageDispatcher);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {

                ch.pipeline()//.addLast("logging",new LoggingHandler(LogLevel.INFO))//for debug
                        .addLast("decoder", new MessageDecoder())
                        .addLast("encoder", new MessageEncoder())
                        .addLast("client-idle-handler", new IdleStateHandler(heartbeatInterval, 0, 0, MILLISECONDS))
                        .addLast("handler", nettyClientHandler);
            }
        });
    }

    public void doConnect() {
        long start = System.currentTimeMillis();
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("connect success host:" + ip + ",port:" + port + "");
                } else {
                    EventLoop loop = future.channel().eventLoop();
                    loop.schedule(new Runnable() {
                        @Override
                        public void run() {
                            NettyClient.this.doConnect();
                        }
                    }, 10, TimeUnit.SECONDS);
                    System.out.println("connect unsuccess will try after 10s host:" + ip + ",port:" + port);
                }
            }
        });
        channel = future.channel();
    }

    protected void doClose() {
        if (channel != null) {
            channel.close();
        }
        if (bootstrap != null) {
            workerGroup.shutdownGracefully().syncUninterruptibly();
        }
    }

    public Channel getChannel() {
        return channel;
    }
}
