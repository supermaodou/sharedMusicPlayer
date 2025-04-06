package org.song.sharedmusicplayer.websocket.enums;

public enum WebSocketMessageType {
    QUEUE_UPDATE,
    CURRENT_PLAYING,
    PLAY_STATE,
    PLAY_PROGRESS,
    SONG_ENDED,
    MUSIC_LIST_UPDATE
}