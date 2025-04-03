package org.song.sharedmusicplayer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.song.sharedmusicplayer.entity.Music;


import java.util.List;

public interface MusicService extends IService<Music> {
    List<Music> getAllMusic();
    void addMusic(Music music);
}
