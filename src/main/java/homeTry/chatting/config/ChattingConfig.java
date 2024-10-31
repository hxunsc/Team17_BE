package homeTry.chatting.config;

import homeTry.chatting.interceptor.StompInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChattingConfig implements WebSocketMessageBrokerConfigurer {
    private final StompInterceptor stompInterceptor;

    public ChattingConfig(StompInterceptor stompInterceptor) {
        this.stompInterceptor = stompInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/chatting/websocket")
                .setAllowedOrigins("http://localhost:63342")
                .withSockJS()
                .setWebSocketEnabled(true)
                .setSessionCookieNeeded(false);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.setApplicationDestinationPrefixes("/pub")
                .setUserDestinationPrefix("/user")
                .enableSimpleBroker("/sub", "/queue");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompInterceptor);
    }
}