package com.xfyh.socket.websocket.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/25 19:04
 */
@Slf4j
@Component
public class ChatMessageHandler extends TextWebSocketHandler {

    private static Map<String,WebSocketSession> users = Collections.synchronizedMap(new HashMap());


    /**
     * 连接成功时候，会触发UI上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("connect to the websocket success......");
        final String userName = getUserName(session);
        if (!users.containsKey(userName)) {
            users.put(userName, session);
        } else {
            super.handleTextMessage(session, new TextMessage(userName+":已在线"));
        }
        // 这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        // TextMessage returnMessage = new TextMessage("你将收到的离线");
        // session.sendMessage(returnMessage);
    }

    /**
     * 在UI在用js调用websocket.send()时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        sendMessageToUsers(message);

    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        for (WebSocketSession user : users.values()) {
            if (user.getAttributes().containsKey(userName)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : users.values()) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        log.debug("websocket connection closed......");
        users.remove(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        log.debug("websocket connection closed......");
       // users.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private String getUserName(WebSocketSession session) {
        final String path = session.getUri().getPath();
        final int i = path.lastIndexOf("/");
        return path.substring(i);

    }

}
