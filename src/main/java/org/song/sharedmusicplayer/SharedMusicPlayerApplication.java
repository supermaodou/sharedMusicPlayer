package org.song.sharedmusicplayer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.song.sharedmusicplayer.mapper")
public class SharedMusicPlayerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedMusicPlayerApplication.class, args);
    }

}
