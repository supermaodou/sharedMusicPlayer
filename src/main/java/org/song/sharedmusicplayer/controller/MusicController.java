package org.song.sharedmusicplayer.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.song.sharedmusicplayer.entity.Music;
import org.song.sharedmusicplayer.service.MusicService;
import org.song.sharedmusicplayer.uitls.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Value("${music.path}")
    private String musicPath;

    @Resource
    private MusicService musicService;

    @GetMapping("/list")
    public Result<List<Music>> getAllMusic() {
        return Result.success(musicService.getMusicList());
    }

    @PostMapping("/add")
    public Result<Boolean> addMusic(@RequestBody Music music) {
        return Result.success(musicService.addMusic(music));
    }

    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteMusic(@PathVariable("id") Long id) {
        return Result.success(musicService.deleteMusic(id));
    }

    @GetMapping("/local/{fileName:.+}")
    public void getLocalMusic(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        // 构建安全路径
        Path basePath = Paths.get(musicPath).toAbsolutePath().normalize();
        Path targetPath = basePath.resolve(fileName).normalize();

        // 检查是否在允许的目录下
        if (!targetPath.startsWith(basePath)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        File file = targetPath.toFile();
        if (!file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("audio/mpeg");
        Files.copy(file.toPath(), response.getOutputStream());
        response.flushBuffer();
    }
}
