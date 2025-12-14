package com.vmcomputron.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // куда сервер будет "пушить" сообщения клиентам
        config.enableSimpleBroker("/topic");

        // куда клиент будет отправлять сообщения на сервер (если нужно)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // SockJS endpoint
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
        // если хочешь без SockJS: registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }
}
