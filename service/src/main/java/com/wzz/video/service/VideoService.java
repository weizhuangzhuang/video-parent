package com.wzz.video.service;

import com.wzz.video.pojo.Comments;
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
     * 分页查询出我喜欢的视频
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);

    PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);

    /**
     * 查询热搜词
     * @return
     */
    public List<String> getHotWords();

    /**
     * 用户喜欢视频
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    public void userLikeVideo(String userId , String videoId , String videoCreaterId);

    /**
     * 用户不喜欢视频/取消点赞
     * @param userId
     * @param videoId
     * @param videoCreaterId
     */
    public void userUnLikeVideo(String userId , String videoId , String videoCreaterId);

    /**
     * 保存留言
     * @param comments
     */
    public void saveComments(Comments comments);

    /**
     * 分页获取留言列表
     * @param videoId
     * @param page
     * @param pageSize
     */
    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
}
