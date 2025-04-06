package org.song.sharedmusicplayer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.song.sharedmusicplayer.entity.Music;
import org.springframework.stereotype.Service;
import org.song.sharedmusicplayer.service.MusicService;
import org.song.sharedmusicplayer.mapper.MusicMapper;
import java.util.List;

@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {

    @Override
    public List<Music> getAllMusic() {
        return list();
    }

    @Override
    public Boolean addMusic(Music music) {
        return save(music);
    }

    @Override
    public Boolean deleteMusic(Long id) {
        return removeById(id);
    }
}
