package org.song.sharedmusicplayer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.song.sharedmusicplayer.entity.User;


public interface UserService extends IService<User> {
    User getOrCreateUser(String username, String computerName);

    String getComputerName();

    boolean updateUsername(Long userId, String newUsername);
}
