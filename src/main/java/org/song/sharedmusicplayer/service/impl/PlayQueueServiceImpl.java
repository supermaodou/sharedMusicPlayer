package org.song.sharedmusicplayer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.song.sharedmusicplayer.entity.PlayQueue;
import org.springframework.stereotype.Service;
import org.song.sharedmusicplayer.service.PlayQueueService;
import org.song.sharedmusicplayer.mapper.PlayQueueMapper;
import java.util.List;

@Service
public class PlayQueueServiceImpl extends ServiceImpl<PlayQueueMapper, PlayQueue> implements PlayQueueService {

    @Override
    public List<PlayQueue> getQueue() {
        return list();
    }

    @Override
    public void addToQueue(Long musicId, Long userId) {
        PlayQueue queue = new PlayQueue();
        queue.setMusicId(musicId);
        queue.setUserId(userId);
        queue.setStatus("waiting");
        save(queue);
    }

    @Override
    public void removeFromQueue(Long queueId) {
        removeById(queueId);
    }
}
