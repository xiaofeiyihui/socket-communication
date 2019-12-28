package com.xfyh.socket.websocket.tomcat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/25 16:28
 */
@RestController
public class WebSocketController {
    @Autowired
    WebSocketServer server;
    @PostMapping("/login")
    public String login(String username,String password) throws IOException {
        //TODO: 校验密码
        server.sendInfo(username + "进入了聊天室!");
        return username;
    }
}
