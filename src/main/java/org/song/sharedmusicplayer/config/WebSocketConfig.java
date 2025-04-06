package org.song.sharedmusicplayer.config;

import lombok.RequiredArgsConstructor;
import org.song.sharedmusicplayer.websocket.handler.MusicWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final MusicWebSocketHandler musicWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(musicWebSocketHandler, "/ws/music")
                .setAllowedOrigins("*");
    }
}
