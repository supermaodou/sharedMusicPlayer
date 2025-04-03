package org.song.sharedmusicplayer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_play_queue")
public class PlayQueue {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long musicId;

    private Long userId;

    // 状态：waiting, playing, played
    private String status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime addTime;
}