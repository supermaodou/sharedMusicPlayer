package org.song.sharedmusicplayer.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.song.sharedmusicplayer.entity.Music;
import org.song.sharedmusicplayer.websocket.enums.WebSocketMessageType;
import org.song.sharedmusicplayer.websocket.handler.MusicWebSocketHandler;
import org.song.sharedmusicplayer.websocket.service.WebSocketMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.song.sharedmusicplayer.service.MusicService;
import org.song.sharedmusicplayer.mapper.MusicMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {

    private final WebSocketMessageService messageService;

    private final MusicMapper musicMapper;

    @Override
    public List<Music> getMusicList() {
        return musicMapper.selectList(null);
    }

    @Override
    public Page<Music> getMusicPage(int page, int size) {
        return musicMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public Boolean addMusic(Music music) {
        // 通知所有客户端
        messageService.broadcastMessage(WebSocketMessageType.MUSIC_LIST_UPDATE, getMusicList());
        return save(music);
    }

    @Override
    public Boolean deleteMusic(Long id) {
        // 通知所有客户端
        messageService.broadcastMessage(WebSocketMessageType.MUSIC_LIST_UPDATE, getMusicList());
        return removeById(id);
    }

}
