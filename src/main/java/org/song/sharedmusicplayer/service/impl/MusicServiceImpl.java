package org.song.sharedmusicplayer.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.song.sharedmusicplayer.entity.Music;
import org.song.sharedmusicplayer.websocket.enums.WebSocketMessageType;
import org.song.sharedmusicplayer.websocket.service.WebSocketMessageService;
import org.springframework.stereotype.Service;
import org.song.sharedmusicplayer.service.MusicService;
import org.song.sharedmusicplayer.mapper.MusicMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Integer scanLocalMusic(String path) {
        Path musicDir = Paths.get(path).toAbsolutePath().normalize();
        if (!Files.isDirectory(musicDir)) {
            throw new RuntimeException("音乐目录不存在：" + path);
        }

        int count = 0;
        try {
            // 遍历目录下的所有音乐文件
            List<Music> musicList = Files.walk(musicDir)
                    .filter(p -> isMusic(p.toString()))
                    .map(p -> {
                        Music music = new Music();
                        String fileName = p.getFileName().toString();
                        // 去掉扩展名
                        String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));

                        // 分割歌曲名和歌手
                        String[] parts = nameWithoutExt.split("-", 2);
                        if (parts.length == 2) {
                            music.setTitle(parts[0].trim());
                            music.setArtist(parts[1].trim());
                        } else {
                            // 如果没有按照规定格式命名，则整个文件名作为标题
                            music.setTitle(nameWithoutExt);
                            music.setArtist("未知歌手");
                        }

                        // 相对路径作为URL
                        String relativePath = musicDir.relativize(p).toString().replace('\\', '/');
                        music.setUrl("http://localhost:8088/music/local/" + relativePath);
                        // 这里可以用第三方库读取音频时长，暂时设为0
                        music.setDuration(0);
                        // 设置为系统添加
                        music.setAddedBy(0L);
                        return music;
                    })
                    .collect(Collectors.toList());

            // 批量保存到数据库
            if (!musicList.isEmpty()) {
                saveBatch(musicList);
                count = musicList.size();
                // 发送WebSocket通知
                messageService.broadcastMessage(WebSocketMessageType.MUSIC_LIST_UPDATE, getMusicList());
            }
        } catch (IOException e) {
            throw new RuntimeException("扫描音乐文件失败", e);
        }

        return count;
    }

    private boolean isMusic(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".mp3") || lower.endsWith(".wav")
                || lower.endsWith(".flac") || lower.endsWith(".m4a")
                || lower.endsWith(".ogg") || lower.endsWith(".aac");
    }

}
