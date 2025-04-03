package org.song.sharedmusicplayer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.song.sharedmusicplayer.entity.PlayQueue;


import java.util.List;

public interface PlayQueueService extends IService<PlayQueue> {
    List<PlayQueue> getQueue();
    void addToQueue(Long musicId, Long userId);
    void removeFromQueue(Long queueId);
}
