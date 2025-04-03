package org.song.sharedmusicplayer.controller;

import jakarta.annotation.Resource;
import org.song.sharedmusicplayer.entity.Music;
import org.song.sharedmusicplayer.service.MusicService;
import org.song.sharedmusicplayer.uitls.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Resource
    private MusicService musicService;

    @GetMapping("/list")
    public Result<List<Music>> getAllMusic() {
        return Result.success(musicService.getAllMusic());
    }

    @PostMapping("/add")
    public void addMusic(@RequestBody Music music) {
        musicService.addMusic(music);
    }
}
