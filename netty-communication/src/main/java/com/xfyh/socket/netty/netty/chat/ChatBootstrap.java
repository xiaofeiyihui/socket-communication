package com.xfyh.socket.netty.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Description: chat启动类
 * @Auther: xfyh
 * @Date: 2019/12/28 21:50
 */
public class ChatBootstrap {
    public static void main(String[] args) {
        final NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        final NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            final ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup,workGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChatWebsocketChannelInitializer());
            System.out.println("服务开开启...");
            final Channel channel = b.bind(8888).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
