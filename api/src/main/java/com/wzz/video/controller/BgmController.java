package com.wzz.video.controller;

import com.wzz.video.pojo.Bgm;
import com.wzz.video.service.BgmService;
import com.wzz.video.utils.VideoJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bgm")
@Api(value = "背景音乐业务的接口", tags = {"背景音乐业务的controller"})
public class BgmController {

    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "背景音乐业务列表" , notes = "背景音乐业务列表的接口")
    @GetMapping("/list")
    public VideoJSONResult upload(){
        List<Bgm> bgms = bgmService.queryBgmList();
        return VideoJSONResult.ok(bgms);
    }

}
