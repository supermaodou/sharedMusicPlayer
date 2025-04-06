package org.song.sharedmusicplayer.controller;

import jakarta.annotation.Resource;
import org.song.sharedmusicplayer.service.PlayQueueService;
import org.song.sharedmusicplayer.uitls.Result;
import org.song.sharedmusicplayer.vo.MusicQueueVO;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/queue")
public class PlayQueueController {

    @Resource
    private PlayQueueService queueService;

    @GetMapping("/list")
    public Result<List<MusicQueueVO>> getQueue() {
        return Result.success(queueService.getQueue());
    }

    @PostMapping("/add")
    public Result<Boolean> addToQueue(@RequestParam Long musicId, @RequestParam Long userId) {
        return Result.success(queueService.addToQueue(musicId, userId));
    }

    @DeleteMapping("/remove/{queueId}")
    public Result<Boolean> removeFromQueue(@PathVariable Long queueId) {
        return Result.success(queueService.removeFromQueue(queueId));
    }

    @GetMapping("/current")
    public Result<MusicQueueVO> getCurrentMusic() {
        return Result.success(queueService.getQueue().get(0));
    }
}
