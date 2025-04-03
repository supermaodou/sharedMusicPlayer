package org.song.sharedmusicplayer.controller;

import jakarta.annotation.Resource;
import org.song.sharedmusicplayer.entity.User;
import org.song.sharedmusicplayer.service.UserService;
import org.song.sharedmusicplayer.uitls.Result;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestParam String username, @RequestParam String computerName) {
        return Result.success(userService.getOrCreateUser(username, computerName));
    }

    @GetMapping("/getComputerName")
    public Result<String> getComputerName() {
        return Result.success(userService.getComputerName());
    }

    @PostMapping("/updateUsername")
    public Result<String> updateUsername(@RequestParam Long userId, @RequestParam String newUsername) {
        boolean success = userService.updateUsername(userId, newUsername);
        return success ? Result.success("修改成功") : Result.error(400, "用户不存在或修改失败");
    }
}
