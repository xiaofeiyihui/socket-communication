package com.xfyh.socket.netty.netty.example.im;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/24 15:28
 */
public class ServerIniterHandler extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //管道注册handler
        ChannelPipeline pipeline = ch.pipeline();
        //编码通道处理
        pipeline.addLast("decode", new StringDecoder());
        //转码通道处理
        pipeline.addLast("encode", new StringEncoder());
        //聊天服务通道处理
        pipeline.addLast("chat", new ServerHandler());
    }
}
