package com.fundingflex.consultation.chat.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.fundingflex.consultation.chat.handler.ChatWebSocketHandler;
import com.fundingflex.consultation.chat.service.ChatService;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, ApplicationContextAware  {

	private ApplicationContext applicationContext;
	
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

	
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(applicationContext.getBean(ChatService.class)), "/ws/chat").setAllowedOrigins("*");
    }
}