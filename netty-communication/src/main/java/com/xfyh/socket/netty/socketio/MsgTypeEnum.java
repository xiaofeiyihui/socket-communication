package com.xfyh.socket.netty.socketio;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/26 19:22
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MsgTypeEnum {

    OFFLINE("offline","下线"),
    ONLINE("online","上线");
    private String value;
    private String desc;


}
