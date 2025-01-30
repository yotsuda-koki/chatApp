package com.example.chatApp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private final MessageService messageService;

	public WebSocketConfig(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new ChatHandler(messageService), "/chatroom") // エンドポイントの設定
				.setAllowedOriginPatterns("*") // 開発段階ですべてのオリジンを許可
				.withSockJS(); // SockJSを有効化
	}
}
