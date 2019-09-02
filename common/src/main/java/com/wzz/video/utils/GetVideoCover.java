package com.wzz.video.utils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

//获取视频封面截图
public class GetVideoCover {

    private  String ffmpegEXE;

    public GetVideoCover(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    //ffmpeg.exe -ss 00:00:01 -i spring.mp4 -vframes 1 bb.jpg
    public void cover(String videoPath ,String outputPath) throws IOException {

        //指令集合
        List<String> commandStr = new ArrayList();

        commandStr.add(ffmpegEXE);

        commandStr.add("-ss");
        commandStr.add("00:00:01");

        commandStr.add("-i");
        commandStr.add(String.valueOf(videoPath));

        commandStr.add("-vframes");
        commandStr.add("1");

        commandStr.add(outputPath);

        ProcessBuilder processBuilder = new ProcessBuilder(commandStr);
        Process process = processBuilder.start();

        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        while((line = bufferedReader.readLine()) != null){

        }
        if(bufferedReader != null){
            bufferedReader.close();
        }
        if(inputStreamReader != null){
            inputStreamReader.close();
        }
        if(errorStream != null){
            errorStream.close();
        }
    }

/*    public static void main(String[] args) throws IOException {
        GetVideoCover getVideoCover = new GetVideoCover("D:/wzz_videos_dev/ffmpeg/bin/ffmpeg.exe");

        getVideoCover.cover("D:/wzz_videos_dev/湖泊.mp4" , "D:/wzz_videos_dev/test.jpg");
    }*/

}
