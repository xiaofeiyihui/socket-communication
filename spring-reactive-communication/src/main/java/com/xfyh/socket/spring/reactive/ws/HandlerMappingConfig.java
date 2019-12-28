package com.xfyh.socket.spring.reactive.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/26 14:02
 */

@Configuration
public class HandlerMappingConfig {

    final Map<String, WebSocketHandler> map = new HashMap<>(1);

    @Bean
    public HandlerMapping webSocketMapping() {
        map.put("/echo", new EchoHandler());
        map.put("/webSocketServerFlux",new ChatHandle());
        final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
        mapping.setUrlMap(map);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
