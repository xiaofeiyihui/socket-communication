package com.xfyh.socket.netty.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/26 19:15
 */
@Component
@Order(1)
@Slf4j
public class SocketServer implements CommandLineRunner {

    /**
     * socketIOServer
     */
    private final SocketIOServer socketIOServer;

    @Autowired
    public SocketServer(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("---------- NettySocket通知服务开始启动 ----------");
        socketIOServer.start();
        log.info("---------- NettySocket通知服务启动成功 ----------");
    }

}
