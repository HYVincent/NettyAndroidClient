package com.shangyi.netty;

import com.shangyi.netty.module.Constants;
import com.shangyi.netty.module.LoginMsg;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;

/**
 * @author 徐飞
 * @version 2016/02/25 14:11
 */
public class NettyClientBootstrap {
    private int port = Config.SERVICE_PORT;
    private String host =Config.SERVICE_IP ;
    public SocketChannel socketChannel;
    private static final EventExecutorGroup group = new DefaultEventExecutorGroup(20);

    public void startNetty() throws InterruptedException {
        if (socketChannel!=null && socketChannel.isOpen()) {
            System.out.println("已经连接");
        }else {
            Constants.setClientId("001");// TODO: 2016/2/23
            System.out.println("长链接开始");
            if (start()) {
                LoginMsg loginMsg = new LoginMsg();// TODO: 2016/2/23
                loginMsg.setPassword("yao");
                loginMsg.setUserName("robin");
                socketChannel.writeAndFlush(loginMsg);
                System.out.println("长链接成功");
            }else {
                System.out.println("长链接失败...");
            }
        }
    }

    private Boolean start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.group(eventLoopGroup);
        bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new IdleStateHandler(20, 10, 0));//10表示多久往服务器写入心跳消息 20表示20秒读取一次服务器消息
                socketChannel.pipeline().addLast(new ObjectEncoder());
                socketChannel.pipeline().addLast(new ObjectDecoder(1024,ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));

                socketChannel.pipeline().addLast(new NettyClientHandler());
            }
        });
        ChannelFuture future = null;
        try {
            future = bootstrap.connect(new InetSocketAddress(host, port)).sync();
            if (future.isSuccess()) {
                socketChannel = (SocketChannel) future.channel();
                System.out.println("connect server  成功---------");
                return true;
            } else {
                System.out.println("connect server  失败---------");
                startNetty();
                return false;
            }
        } catch (Exception e) {
            System.out.println("无法连接----------------");
            return false;
        }
    }

    public void closeChannel() {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }

    public boolean isOpen() {
        if (socketChannel != null) {
            System.out.println(socketChannel.isOpen());
            return socketChannel.isOpen();
        }
        return false;
    }
}