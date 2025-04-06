package org.song.sharedmusicplayer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.song.sharedmusicplayer.entity.PlayQueue;
import org.song.sharedmusicplayer.vo.MusicQueueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.song.sharedmusicplayer.service.PlayQueueService;
import org.song.sharedmusicplayer.mapper.PlayQueueMapper;

import java.util.List;

@Service
public class PlayQueueServiceImpl extends ServiceImpl<PlayQueueMapper, PlayQueue> implements PlayQueueService {

    @Autowired
    private PlayQueueMapper playQueueMapper;

    @Override
    public List<MusicQueueVO> getQueue() {
        return playQueueMapper.findCurrentQueue();
    }

    @Override
    public Boolean addToQueue(Long musicId, Long userId) {
        PlayQueue queue = new PlayQueue();
        queue.setMusicId(musicId);
        queue.setUserId(userId);
        queue.setStatus("waiting");
        return save(queue);
    }

    @Override
    public Boolean removeFromQueue(Long queueId) {
        return removeById(queueId);
    }
}
