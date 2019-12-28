package com.xfyh.socket.spring.reactive.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/26 14:42
 */
@Component
public class ChatHandle implements WebSocketHandler {

    private static Map<String,WebSocketSession> users = Collections.synchronizedMap(new HashMap());
    /**
     *
     * @param session
     * @return
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        users.put(getUserName(session),session);
        return session.send(
                session.receive()
                        .map(msg -> session.textMessage(
                                "服务端返回：小明， -> " + msg.getPayloadAsText())));
    }


    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(String message) {
        for (WebSocketSession user : users.values()) {
            try {
                user.textMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getUserName(WebSocketSession session) {
        final String path = session.getHandshakeInfo().getUri().getPath();
        final int i = path.lastIndexOf("/");
        return path.substring(i);

    }


}
