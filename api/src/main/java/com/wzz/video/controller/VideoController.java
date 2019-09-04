package com.wzz.video.controller;

import com.wzz.video.pojo.Videos;
import com.wzz.video.service.BgmService;
import com.wzz.video.service.VideoService;
import com.wzz.video.utils.*;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@RestController
@Api(value = "用户上传视频的接口", tags = {"用户上传视频的controller"})
@RequestMapping("/video")
public class VideoController extends BasicController{

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;


    @ApiOperation(value = "用户上传视频" , notes = "用户视频上传的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
            dataType = "String" ,paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id", required = false,
                    dataType = "String" ,paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "视频秒数", required = true,
                    dataType = "double" ,paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "int" ,paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频长度", required = true,
                    dataType = "int" ,paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String" ,paramType = "form"),
    })
    @PostMapping(value = "/uploadVideo" , headers = "content-type=multipart/form-data")
    public VideoJSONResult uploadVideo(String userId ,String bgmId, float videoSeconds,
                                       int videoWidth, int videoHeight,String desc,
                                       @ApiParam(value = "短视频", required = true)
                                       MultipartFile file) throws IOException {
        if(StringUtils.isBlank(userId)){
            return VideoJSONResult.errorMsg("用户ID不能为空");
        }

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        //视频保存在数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video/";
        //视频截图保存在数据库中的相对路径
        String uploadCoverPathDB = "";
        //文件上传的最终路径
        String finalVideoPath = "";
        try{
            if(file != null){
                String fileName = file.getOriginalFilename();
                if(StringUtils.isNotBlank(fileName)){
                    //设置数据库保存路径
                    uploadPathDB += fileName;
                    finalVideoPath = FILE_SPACE + uploadPathDB;
                    //判断父目录是否存在
                    File outFile = new File(finalVideoPath);
                    //如果文件的父目录不为空或者不是目录
                    if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }else {
                    return VideoJSONResult.errorMsg("上传发生错误....");
                }
            }}catch (Exception e){
            e.printStackTrace();
            return VideoJSONResult.errorMsg("上传发生错误....");
        }finally {
            if(fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        if(StringUtils.isNotBlank(bgmId)){
            String bgmPath = FILE_SPACE + bgmService.queryBgmById(bgmId).getPath();
            MergeVideoMp3 mergeVideoMp3 = new MergeVideoMp3(FFMPEG_EXE);
            String videoInputPath = finalVideoPath;
            //190805GYC4AY8ACH/video/wx4153c49fe4c97e67.o6zAJs55FvTNwkjHu2VwR5xTB49c.9lMQpSgOmWNtf79db07f6e84043cff0b1afa23943b52.jpg
            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            uploadPathDB ="/" + userId + "/video/" + videoOutputName;
            finalVideoPath = FILE_SPACE + uploadPathDB;
            mergeVideoMp3.convert(videoInputPath,bgmPath,videoSeconds,finalVideoPath);
        }

        GetVideoCover getVideoCover = new GetVideoCover(FFMPEG_EXE);
        int index = uploadPathDB.lastIndexOf(".");
        uploadCoverPathDB = uploadPathDB.substring(0 , index) + ".jpg";
        getVideoCover.cover(finalVideoPath,FILE_SPACE + uploadCoverPathDB);

        System.out.println(uploadPathDB);
        System.out.println(uploadCoverPathDB);
        System.out.println(finalVideoPath);

        //保存视频信息到数据库
        Videos videos = new Videos();
        videos.setUserId(userId);
        videos.setAudioId(bgmId);
        videos.setVideoDesc(desc);
        videos.setVideoPath(uploadPathDB);
        videos.setVideoSeconds(videoSeconds);
        videos.setVideoHeight(videoHeight);
        videos.setVideoWidth(videoWidth);
        videos.setStatus(VideoStatusEnum.SUCCESS.value);
        videos.setCreateTime(new Date());
        videos.setCoverPath(uploadCoverPathDB);

        String videoId = videoService.saveVideos(videos);

        //返回视频id
        return VideoJSONResult.ok(videoId);
    }

    @ApiOperation(value = "用户上传封面" , notes = "用户封面上传的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String" ,paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频id", required = true,
                    dataType = "String" ,paramType = "form"),
    })
    @PostMapping(value = "/uploadCover" , headers = "content-type=multipart/form-data")
    public VideoJSONResult uploadVideo(String userId , String videoId,
                                       @ApiParam(value = "短视频", required = true)
                                               MultipartFile file) throws IOException {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)){
            return VideoJSONResult.errorMsg("用户ID和视频ID不能为空");
        }

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        //保存在数据库中的相对路径
        String uploadPathDB = "/" + userId + "/video/";
        //文件上传的最终路径
        String finalCoverPath = "";
        try{
            if(file != null){
                String fileName = file.getOriginalFilename();
                if(StringUtils.isNotBlank(fileName)){
                    //设置数据库保存路径
                    uploadPathDB =  uploadPathDB + fileName;
                    finalCoverPath = FILE_SPACE + uploadPathDB + fileName;
                    //判断父目录是否存在
                    File outFile = new File(finalCoverPath);
                    //如果文件的父目录不为空或者不是目录
                    if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()){
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }else {
                    return VideoJSONResult.errorMsg("上传发生错误....");
                }
            }}catch (Exception e){
            e.printStackTrace();
            return VideoJSONResult.errorMsg("上传发生错误....");
        }finally {
            if(fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        videoService.updateVideos(videoId,uploadPathDB);
        return VideoJSONResult.ok();
    }

    /**
     * 分页和搜索查询视频列表，保存热搜词
     * @param videos
     * @param isSaveRecord  1 - 需要保存    0 - 不需要保存，或者为空的时候
     * @param page
     * @return
     */
    @PostMapping("/showAll")
    public VideoJSONResult showAll(@RequestBody Videos videos , Integer isSaveRecord ,Integer page){

        if(page == null){
            page = 1;
        }

        //System.out.println(page);
        PagedResult allVideos = videoService.getAllVideos(videos , isSaveRecord , page, PAGE_SIZE);
        //System.out.println(allVideos.getTotal());
        return VideoJSONResult.ok(allVideos);
    }

    @PostMapping("/hot")
    public VideoJSONResult hot(){
        return VideoJSONResult.ok(videoService.getHotWords());
    }

}
