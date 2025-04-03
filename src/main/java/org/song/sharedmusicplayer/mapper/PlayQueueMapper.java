package org.song.sharedmusicplayer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.song.sharedmusicplayer.entity.PlayQueue;

import java.util.List;

@Mapper
public interface PlayQueueMapper extends BaseMapper<PlayQueue> {
}