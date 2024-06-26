package com.example;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;

    private volatile Channel channel;
    private int timeout = 2000;
    private int heartbeatInterval = 8 * 1000;
    private String ip;
    private int port;

    public NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void doOpen() {
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2,
                new DefaultThreadFactory("NettyServerWorker", true));
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

                ch.pipeline()//.addLast("logging", new LoggingHandler(LogLevel.DEBUG))//for debug
                        .addLast("encoder", new MessageEncoder())
                        .addLast("decoder", new MessageDecoder())
                        .addLast("client-idle-handler", new IdleStateHandler(heartbeatInterval, 0, 0, MILLISECONDS))
                        .addLast("handler", nettyClientHandler);
            }
        });
    }

    public void doConnect() throws InterruptedException {
        ChannelFuture future = bootstrap
                .connect(new InetSocketAddress(ip, port), new InetSocketAddress("127.0.0.1", 0));
        ChannelFuture channelFuture = future.awaitUninterruptibly();
        Channel channel = channelFuture.channel();
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    if (null != NettyClient.this.channel) {
                        NettyClient.this.channel.close();
                    }
                    NettyClient.this.channel = future.sync().channel();
                    System.out.println("connect success host:" + ip + ",port:" + port + "");
                } else {
                    EventLoop loop = future.channel().eventLoop();
                    loop.schedule(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                NettyClient.this.doConnect();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }, 10, TimeUnit.SECONDS);
                    System.out.println("connect unsuccess will try after 10s host:" + ip + ",port:" + port);
                }
            }
        }).sync();
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
        if (channel.isActive() && channel.isOpen()) {
            return channel;
        }
        return null;
    }

    /**
     * 异步，不关心结果
     *
     * @param channel
     * @param msg
     */
    public void send(Channel channel, Object msg) {
        channel.writeAndFlush(msg);
    }


    public CommonProtocol send(Channel channel, CommonProtocol msg, int timeout) {
        CompletableFuture<CommonProtocol> future = new CompletableFuture<>();
        channel.attr(NettyClientHandler.attributeKey).get().put(msg.getInvokerId(), future);
        try {
            ChannelFuture channelFuture = channel.writeAndFlush(msg);
            boolean success = channelFuture.awaitUninterruptibly(timeout);
            if (!success) {
                throw new RuntimeException("Failed to send message   to " + channel.remoteAddress()
                        + " in timeout(" + timeout + "ms) error");
            }
            Throwable cause = channelFuture.cause();
            if (null != cause) {
                throw cause;
            }
            return future.get(timeout, MILLISECONDS);
        } catch (Throwable throwable) {
            throw new RuntimeException("Failed to send message   to " + channel.remoteAddress()
                    + " cause (" + throwable.getMessage() + ") ");
        } finally {
            channel.attr(NettyClientHandler.attributeKey).get().remove(msg.getInvokerId());
        }

    }
}
