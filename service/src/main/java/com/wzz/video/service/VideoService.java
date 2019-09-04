package com.wzz.video.service;

import com.wzz.video.pojo.Videos;
import com.wzz.video.utils.PagedResult;

import java.util.List;

public interface VideoService {

    /**
     * 新增上传的视频
     */
    public String saveVideos(Videos videos);

    public void updateVideos(String videoId , String coverPath);

    /**
     * 分页查询视频列表
     * @param page   查几页
     * @param pageSize  每页的记录数
     * @return
     */
    public PagedResult getAllVideos(Videos videos , Integer isSaveRecord , Integer page , Integer pageSize);

    /**
     * 查询热搜词
     * @return
     */
    public List<String> getHotWords();

}
