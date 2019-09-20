package com.wzz.video.controller;

import com.wzz.video.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @Autowired
    public RedisOperator redisOperator;

    public static final String USER_REDIS_SESSION = "user-redis-session";

    public static final String FFMPEG_EXE = "C:/wzz_videos_dev/ffmpeg/bin/ffmpeg.exe";

    //所有文件的上传路径
    public static final String FILE_SPACE = "C:/wzz_videos_dev";

    public static final Integer PAGE_SIZE = 5;

}
