package org.song.sharedmusicplayer.controller;

import jakarta.annotation.Resource;
import org.song.sharedmusicplayer.entity.PlayQueue;
import org.song.sharedmusicplayer.service.PlayQueueService;
import org.song.sharedmusicplayer.uitls.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/queue")
public class PlayQueueController {

    @Resource
    private PlayQueueService queueService;

    @GetMapping("/list")
    public Result<List<PlayQueue>> getQueue() {
        return Result.success(queueService.getQueue());
    }

    @PostMapping("/add")
    public void addToQueue(@RequestParam Long musicId, @RequestParam Long userId) {
        queueService.addToQueue(musicId, userId);
    }

    @PostMapping("/remove")
    public void removeFromQueue(@RequestParam Long queueId) {
        queueService.removeFromQueue(queueId);
    }
}
