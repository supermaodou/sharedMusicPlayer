package org.song.sharedmusicplayer.controller;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//@RestController
//@RequestMapping("/ws")
public class MusicWebSocket {
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//
//    @OnOpen
//    public void onOpen(WebSocketSession session) {
//        sessions.add(session);
//    }
//
//    @OnClose
//    public void onClose(WebSocketSession session) {
//        sessions.remove(session);
//    }
//
//    public void sendUpdate(String message) throws IOException {
//        for (WebSocketSession session : sessions) {
//            session.sendMessage(new TextMessage(message));
//        }
//    }
}
