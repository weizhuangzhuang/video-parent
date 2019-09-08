package com.wzz.video.controller;


import com.wzz.video.pojo.Users;
import com.wzz.video.pojo.vo.PublisherVideo;
import com.wzz.video.pojo.vo.UsersVO;
import com.wzz.video.service.UserService;
import com.wzz.video.utils.VideoJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@Api(value = "用户相关业务的接口", tags = {"用户相关业务的controller"})
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @ApiOperation(value = "用户头像上传" , notes = "用户头像上传的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String" ,paramType = "query")
    @PostMapping("/uploadFace")
    public VideoJSONResult uploadFace(String userId , @RequestParam("file") MultipartFile[] files) throws IOException {

        if(StringUtils.isBlank(userId)){
            return VideoJSONResult.errorMsg("用户ID不能为空");
        }

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        //所有文件的上传路径
        String fileSpace = "D:/wzz_videos_dev";
        //保存在数据库中的相对路径
        String uploadPathDB = "/" + userId + "/face/";
        try{

        if(files != null & files.length > 0){

            String fileName = files[0].getOriginalFilename();
            if(StringUtils.isNotBlank(fileName)){
                //文件上传的最终路径
                String finalFacePath = fileSpace + uploadPathDB + fileName;
                //设置数据库保存路径
                uploadPathDB += fileName;
                //判断父目录是否存在
                File outFile = new File(finalFacePath);
                //如果文件的父目录不为空或者不是目录
                if(outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()){
                    //创建父文件夹
                    outFile.getParentFile().mkdirs();
                }

                fileOutputStream = new FileOutputStream(outFile);
                inputStream = files[0].getInputStream();
                IOUtils.copy(inputStream,fileOutputStream);
            }else {
                return VideoJSONResult.errorMsg("上传错误....");
            }
        }}catch (Exception e){
            e.printStackTrace();
            return VideoJSONResult.errorMsg("上传错误....");
        }finally {
            if(fileOutputStream != null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        userService.updataUserInfo(user);
        return VideoJSONResult.ok(uploadPathDB);
    }

    @ApiOperation(value = "用户信息查询" , notes = "用户信息查询的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String" ,paramType = "query")
    @PostMapping("/query")
    public VideoJSONResult queryUserInfo(String userId){
        if(StringUtils.isBlank(userId)){
            return VideoJSONResult.errorMsg("用户ID不能为空");
        }
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userService.queryUserInfo(userId),usersVO);
        return VideoJSONResult.ok(usersVO);
    }

    /**
     *  查询发布视频者
     * @param loginUserId  登录用户的ID
     * @param videoId      视频ID
     * @param publishUserId
     * @return
     */
    @PostMapping("/queryPubilsher")
    public VideoJSONResult queryPubilsher(String loginUserId , String videoId , String publishUserId){
        if(StringUtils.isAllBlank(publishUserId)){
            return VideoJSONResult.errorMsg("");
        }
        //查询视频发布者的信息
        Users userInfo = userService.queryUserInfo(publishUserId);
        UsersVO publisher = new UsersVO();
        BeanUtils.copyProperties(userInfo , publisher);

        //查询当前登录者和视频的点赞关系
        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId,videoId);

        PublisherVideo bean = new PublisherVideo();
        bean.setPublisher(publisher);
        bean.setUserLikeVideo(userLikeVideo);

        return VideoJSONResult.ok(bean);
    }

}
