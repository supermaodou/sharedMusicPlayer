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
}
