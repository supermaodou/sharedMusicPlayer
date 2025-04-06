package org.song.sharedmusicplayer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.song.sharedmusicplayer.entity.PlayQueue;
import org.song.sharedmusicplayer.vo.MusicQueueVO;


import java.util.List;

public interface PlayQueueService extends IService<PlayQueue> {
    List<MusicQueueVO> getQueue();
    Boolean addToQueue(Long musicId, Long userId);
    Boolean removeFromQueue(Long queueId);
}
