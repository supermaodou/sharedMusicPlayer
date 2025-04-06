package org.song.sharedmusicplayer.vo;

import lombok.Data;

@Data
public class MusicQueueVO {
    private Long queueId;    // 队列ID
    private Long musicId;         // 歌曲ID
    private String title;    // 歌曲名
    private String artist;   // 艺术家
    private Long addedBy;    // 添加者 ID
    private String addedByUsername; // 添加者用户名
}