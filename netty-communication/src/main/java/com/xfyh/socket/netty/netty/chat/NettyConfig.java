package com.xfyh.socket.netty.netty.chat;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Description: chat全局配置
 * @Auther: xfyh
 * @Date: 2019/12/28 21:12
 */
public class NettyConfig {
    /**
     * 存放每个客户端接入进来的channel对象
     */
    public static ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
