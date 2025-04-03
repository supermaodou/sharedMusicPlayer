package org.song.sharedmusicplayer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.song.sharedmusicplayer.entity.User;
import org.song.sharedmusicplayer.service.UserService;
import org.springframework.stereotype.Service;
import org.song.sharedmusicplayer.mapper.UserMapper;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getOrCreateUser(String username, String computerName) {
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setComputerName(computerName);
            save(user);
        }
        return user;
    }

    @Override
    public String getComputerName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown Computer";
        }
    }

    @Override
    public boolean updateUsername(Long userId, String newUsername) {
        User user = getById(userId);
        if (user == null) {
            return false;
        }
        user.setUsername(newUsername);
        return updateById(user);
    }
}
