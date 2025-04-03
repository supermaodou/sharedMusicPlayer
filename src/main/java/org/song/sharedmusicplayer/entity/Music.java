package org.song.sharedmusicplayer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("tb_music")
public class Music {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String artist;

    private String url;

    private Integer duration;

    private Long addedBy;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime addTime; // 添加时间
}