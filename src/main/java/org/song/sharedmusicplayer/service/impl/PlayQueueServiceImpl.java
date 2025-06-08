package org.song.sharedmusicplayer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.song.sharedmusicplayer.entity.Music;
import org.song.sharedmusicplayer.entity.PlayQueue;
import org.song.sharedmusicplayer.websocket.enums.WebSocketMessageType;
import org.song.sharedmusicplayer.websocket.handler.MusicWebSocketHandler;
import org.song.sharedmusicplayer.vo.MusicQueueVO;
import org.song.sharedmusicplayer.websocket.service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.song.sharedmusicplayer.service.PlayQueueService;
import org.song.sharedmusicplayer.mapper.PlayQueueMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayQueueServiceImpl extends ServiceImpl<PlayQueueMapper, PlayQueue> implements PlayQueueService {

    private final PlayQueueMapper playQueueMapper;
    private final WebSocketMessageService messageService;

    @Override
    public List<MusicQueueVO> getQueue() {
        // 获取当前队列
        List<MusicQueueVO> queue = playQueueMapper.findCurrentQueue();
        // 使用LinkedHashSet去重，保持原有顺序
        return queue.stream()
                .distinct()
                .toList();
    }

    @Override
    public Boolean addToQueue(Long musicId, Long userId) {
        PlayQueue queue = new PlayQueue();
        queue.setMusicId(musicId);
        queue.setUserId(userId);
        queue.setStatus("waiting");
        boolean save = save(queue);
        if (save) {
            // 通知所有客户端
            messageService.broadcastMessage(WebSocketMessageType.QUEUE_UPDATE, getQueueList());
            return true;
        }
        return false;
    }

    @Override
    public Boolean removeFromQueue(Long queueId) {
        boolean b = removeById(queueId);
        if (b) {
            // 通知所有客户端
            messageService.broadcastMessage(WebSocketMessageType.QUEUE_UPDATE, getQueueList());
            return true;
        }
        return false;
    }

    @Override
    public void playNext() {
        // 播放下一首歌的逻辑
        PlayQueue currentQueue = playQueueMapper.findFirstByOrderByIdAsc();
        if (currentQueue != null) {
            messageService.broadcastMessage(WebSocketMessageType.CURRENT_PLAYING, currentQueue);
            QueryWrapper<PlayQueue> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", currentQueue.getId());
            playQueueMapper.delete(queryWrapper);
            messageService.broadcastMessage(WebSocketMessageType.QUEUE_UPDATE, getQueueList());
        }
    }

    @Override
    public List<PlayQueue> getQueueList() {
        return playQueueMapper.selectList(null);
    }

    @Override
    public Music getCurrentPlaying() {
        Music music = new Music();
        music.setId(6L);
        music.setTitle("成都");
        music.setArtist("赵雷");
        music.setUrl("https://music.163.com/#/search/m/?s=%E6%88%90%E9%83%BD&type=1");
        music.setDuration(300);
        music.setAddedBy(1L);
        return music;
    }

    @Override
    public void sendInitialState() {
        // 发送当前队列状态
        messageService.broadcastMessage(WebSocketMessageType.QUEUE_UPDATE, getQueueList());
        // 发送当前播放状态
        PlayQueue currentQueue = playQueueMapper.findFirstByOrderByIdAsc();
        if (currentQueue != null) {
            messageService.broadcastMessage(WebSocketMessageType.CURRENT_PLAYING, currentQueue);
        }
    }
}
