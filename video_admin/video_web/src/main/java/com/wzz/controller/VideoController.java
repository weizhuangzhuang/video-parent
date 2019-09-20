package com.wzz.controller;

import com.wzz.pojo.Bgm;
import com.wzz.service.VideoService;
import com.wzz.utils.PagedResult;
import com.wzz.utils.VideoJSONResult;
import com.wzz.utils.VideoStatusEnum;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Controller
@RequestMapping("video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Value("${FILE_SPACE}")
    private String fileSpace;

    @GetMapping("/showAddBgm")
    public String showAddBgm() {
        return "video/addBgm";
    }

    @GetMapping("/showBgmList")
    public String showBgmList() {
        return "video/bgmList";
    }

    @GetMapping("/showReportList")
    public String showReportList() {
        return "video/reportList";
    }

    @PostMapping("/reportList")
    @ResponseBody
    public PagedResult reportList(Integer page) {
        return videoService.queryReportList(page, 10);
    }

    @PostMapping("/queryBgmList")
    @ResponseBody
    public PagedResult queryBgmList(Integer page) {
        return videoService.queryBgmList(page, 10);
    }

    @PostMapping("/forbidVideo")
    @ResponseBody
    public VideoJSONResult forbidVideo(String videoId) {

        videoService.updateVideoStatus(videoId, VideoStatusEnum.FORBID.value);
        return VideoJSONResult.ok();
    }

    @PostMapping("/addBgm")
    @ResponseBody
    public VideoJSONResult addBgm(Bgm bgm) {

        videoService.addBgm(bgm);
        return VideoJSONResult.ok();
    }

    @PostMapping("/delBgm")
    @ResponseBody
    public VideoJSONResult delBgm(String bgmId) {
        videoService.deleteBgm(bgmId);
        return VideoJSONResult.ok();
    }

    @PostMapping("bgmUpload")
    @ResponseBody
    public VideoJSONResult bgmUpload(@RequestParam("file") MultipartFile[] files) throws Exception {
        //文件保存的命名空间
        //String fileSpace = File.separator + "wzz_videos_dev" + File.separator + "mvc-bgm";
        //String fileSpace = "D:" + File.separator + "wzz_videos_dev" + File.separator + "mvc-bgm";

        //保存到数据库的路径
        String uploadPathDB = File.separator + "bgm";

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {

                String fileName = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    // 文件上传的最终保存路径
                    String finalPath = fileSpace + uploadPathDB + File.separator + fileName;
                    // 设置数据库保存的路径
                    uploadPathDB += (File.separator + fileName);

                    File outFile = new File(finalPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } else {
                return VideoJSONResult.errorMsg("上传出错...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return VideoJSONResult.errorMsg("上传出错...");
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return VideoJSONResult.ok(uploadPathDB);
    }

}


