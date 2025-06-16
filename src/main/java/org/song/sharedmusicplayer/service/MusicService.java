package org.song.sharedmusicplayer.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.song.sharedmusicplayer.entity.Music;

import java.util.List;

public interface MusicService extends IService<Music> {
    List<Music> getMusicList();

    Page<Music> getMusicPage(int page, int size);

    Boolean addMusic(Music music);

    Boolean deleteMusic(Long id);

    Integer scanLocalMusic(String path);
}
