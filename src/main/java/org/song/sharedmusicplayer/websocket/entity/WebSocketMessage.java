package org.song.sharedmusicplayer.websocket.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.sharedmusicplayer.websocket.enums.WebSocketMessageType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {
    private WebSocketMessageType type;
    private Object data;
}