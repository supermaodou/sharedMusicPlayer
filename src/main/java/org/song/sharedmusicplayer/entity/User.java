package org.song.sharedmusicplayer.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String computerName;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
