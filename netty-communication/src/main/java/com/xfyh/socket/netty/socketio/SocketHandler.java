package com.xfyh.socket.netty.socketio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/26 19:19
 */
@Component
@Slf4j
public class SocketHandler {
    /**
     * ConcurrentHashMap保存当前SocketServer用户ID对应关系
     */
    private Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>(16);


    /**
     * socketIOServer
     */
    private final SocketIOServer socketIOServer;

    @Autowired
    public SocketHandler(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    /**
     * 当客户端发起连接时调用
     *
     * @param socketIOClient
     * @return void
     * @author xfyh
     */
    @OnConnect
    public void onConnect(SocketIOClient socketIOClient) {
        String userName = socketIOClient.getHandshakeData().getSingleUrlParam("userName");
        if (StringUtils.isNotBlank(userName)) {
            log.info("用户{}开启长连接通知, NettySocketSessionId: {}, NettySocketRemoteAddress: {}",
                    userName, socketIOClient.getSessionId().toString(), socketIOClient.getRemoteAddress().toString());
            // 保存
            clientMap.put(userName, socketIOClient);
            // 发送上线通知
            this.sendMsg(null, null,
                    new MessageDto(userName, null, MsgTypeEnum.ONLINE.getValue(), null));

        }
    }

    /**
     * 客户端断开连接时调用，刷新客户端信息
     *
     * @param socketIOClient
     * @return void
     * @author xfyh
     */
    @OnDisconnect
    public void onDisConnect(SocketIOClient socketIOClient) {
        String userName = socketIOClient.getHandshakeData().getSingleUrlParam("userName");
        if (StringUtils.isNotBlank(userName)) {
            log.info("用户{}断开长连接通知, NettySocketSessionId: {}, NettySocketRemoteAddress: {}",
                    userName, socketIOClient.getSessionId().toString(), socketIOClient.getRemoteAddress().toString());
            // 移除
            clientMap.remove(userName);
            // 发送下线通知
            this.sendMsg(null, null,
                    new MessageDto(userName, null, MsgTypeEnum.OFFLINE.getValue(), "上线通知"));
        }
    }

    /**
     * sendMsg发送消息事件
     *
     * @param socketIOClient
     * @param ackRequest
     * @param messageDto
     * @return void
     * @author xfyh
     */
    @OnEvent("sendMsg")
    public void sendMsg(SocketIOClient socketIOClient, AckRequest ackRequest, MessageDto messageDto) {
        if (messageDto != null) {
            // 全部发送
            final String msgType = messageDto.getMsgType();
            final String targetUserName = messageDto.getTargetUserName();
            if (StringUtils.isBlank(targetUserName)) {
                clientMap.forEach((key, value) -> {
                    if (value != null) {
                        value.sendEvent(msgType+" : ", messageDto);
                    }
                });
            } else {
                final SocketIOClient targetSocketIOClient = clientMap.get(targetUserName);
                if (targetSocketIOClient!=null) {
                    targetSocketIOClient.sendEvent(msgType+" : ",messageDto);
                }
            }
        }
    }
}
