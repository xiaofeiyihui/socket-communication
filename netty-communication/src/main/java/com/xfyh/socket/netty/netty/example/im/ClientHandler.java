package com.xfyh.socket.netty.netty.example.im;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/24 13:51
 */
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //打印服务端的发送数据
        System.out.println(msg);
    }
}
