package org.song.sharedmusicplayer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.song.sharedmusicplayer.entity.User;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}