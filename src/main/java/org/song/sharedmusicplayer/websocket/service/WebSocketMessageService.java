package org.song.sharedmusicplayer.websocket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.song.sharedmusicplayer.service.MusicService;
import org.song.sharedmusicplayer.service.PlayQueueService;
import org.song.sharedmusicplayer.websocket.entity.WebSocketMessage;
import org.song.sharedmusicplayer.websocket.enums.WebSocketMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
@Slf4j
public class WebSocketMessageService {
    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper objectMapper;

    public WebSocketMessageService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void broadcastMessage(WebSocketMessageType type, Object data) {
        WebSocketMessage message = new WebSocketMessage(type, data);
        try {
            String payload = objectMapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(payload);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(textMessage);
                }
            }
        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
    }
}