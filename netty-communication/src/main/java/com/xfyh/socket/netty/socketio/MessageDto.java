package com.xfyh.socket.netty.socketio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/26 19:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    /**
     * 源客户端用户名
     */
    private String sourceUserName;

    /**
     * 目标客户端用户名
     */
    private String targetUserName;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String msgContent;
}
