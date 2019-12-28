package com.xfyh.socket.netty.socketio;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Auther: xfyh
 * @Date: 2019/12/26 19:43
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/view.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
