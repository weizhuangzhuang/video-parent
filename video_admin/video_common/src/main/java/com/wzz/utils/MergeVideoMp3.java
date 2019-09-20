package com.wzz.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 视音频处理类
 */
public class MergeVideoMp3 {
    //ffmpeg.exe -i input.mp4 -c:v copy -an  input-no-audio.mp4
    //$ ffmpeg -i input.mp4 -i demo.mp3 output.avi -t 7 -y
    private  String ffmpegEXE;

    public MergeVideoMp3(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public  String removeAudio(String videoPath) throws IOException {
        List<String> command = new ArrayList();
        File file = new File(videoPath);
        String videonoAudioPath = file.getParent() + "/new" + file.getName();
        file.createNewFile();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoPath);
        command.add("-c:v");
        command.add("copy");
        command.add("-an");
        command.add(videonoAudioPath);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
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
        return videonoAudioPath;
    }

    public void convert(String videoPath , String mp3Path , float seconds , String videoOutputPath) throws IOException {

        String videonoAudioPath = removeAudio(videoPath);

        //指令集合
        List<String> commandStr = new ArrayList();

        commandStr.add(ffmpegEXE);

        commandStr.add("-i");
        commandStr.add(videonoAudioPath);

        commandStr.add("-i");
        commandStr.add(mp3Path);

        commandStr.add("-t");
        commandStr.add(String.valueOf(seconds));

        commandStr.add("-y");
        commandStr.add(videoOutputPath);

        ProcessBuilder processBuilder = new ProcessBuilder(commandStr);
        Process process = processBuilder.start();

        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = "";
        while((line = bufferedReader.readLine()) != null){

        }
        File file = new File(videonoAudioPath);
        file.delete();
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

    public static void main(String[] args) throws IOException {
        //MergeVideoMp3 mergeVideoMp3 = new MergeVideoMp3("D:/wzz_videos_dev/ffmpeg/bin/ffmpeg.exe");
        //mergeVideoMp3.convert();
        //mergeVideoMp3.convert("D:/wzz_videos_dev/湖泊.mp4","D:/wzz_videos_dev/carryon.mp3",
                //10.1,"D:/wzz_videos_dev/demo.mp4");
        //mergeVideoMp3.removeAudio("D:/wzz_videos_dev/湖泊.mp4");
        //mergeVideoMp3.test("D:/wzz_videos_dev/湖泊.mp4");


        String str = "//190805GYC4AY8ACH/video/wx4153c49fe4c97e67.o6zAJs55FvTNwkjHu2VwR5xTB49c.9lMQpSgOmWNtf79db07f6e84043cff0b1afa23943b52.jpg";


        int index2 = str.lastIndexOf(".");

        String newStr = str.substring(0 , index2);

        System.out.println(newStr);

    }

/*    public void test(String path){
        File file = new File(path);

        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getParent());

        File newFile = new File(file.getParent() + "/new" + file.getName());
        System.out.println(newFile.getAbsolutePath());
    }*/


}
