package com.xfyh.socket.spring.reactive.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/26 11:30
 */

@RestController
public class UserController {

    @Autowired
    ChatHandle handler;

    @PostMapping("/login")
    public String login(String username, String password) throws IOException {
        //TODO: 校验密码
        handler.sendMessageToUsers(username + "进入了聊天室!");
        return username;
    }


}
