package org.song.sharedmusicplayer.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.song.sharedmusicplayer.websocket.enums.WebSocketMessageType;
import org.song.sharedmusicplayer.service.MusicService;
import org.song.sharedmusicplayer.service.PlayQueueService;
import org.song.sharedmusicplayer.websocket.service.WebSocketMessageService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.song.sharedmusicplayer.websocket.entity.WebSocketMessage;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.song.sharedmusicplayer.websocket.enums.WebSocketMessageType.PLAY_PROGRESS;
import static org.song.sharedmusicplayer.websocket.enums.WebSocketMessageType.PLAY_STATE;


@Component
@Slf4j
@RequiredArgsConstructor
public class MusicWebSocketHandler extends TextWebSocketHandler {
    private final WebSocketMessageService messageService;
    private final PlayQueueService queueService;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        messageService.addSession(session);
        log.info("新的 WebSocket 连接：{}", session.getId());

        // 发送初始状态
        queueService.sendInitialState();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        WebSocketMessage wsMessage = objectMapper.readValue(payload, WebSocketMessage.class);

        switch (wsMessage.getType()) {
            case PLAY_STATE:
            case PLAY_PROGRESS:
            case SONG_ENDED:
                messageService.broadcastMessage(wsMessage.getType(), wsMessage.getData());
                break;
            default:
                log.warn("未处理的消息类型: {}", wsMessage.getType());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        messageService.removeSession(session);
        log.info("WebSocket 连接关闭：{}", session.getId());
    }
}