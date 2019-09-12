package com.wzz.video.mapper;

import com.wzz.video.pojo.Videos;
import com.wzz.video.pojo.vo.VideosVO;
import com.wzz.video.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    /**
     * @Description: 条件查询所有视频列表
     */
    public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc , @Param("userId") String userId);

    /**
     * 查询点赞视频
     * @param userId
     * @return
     */
    public List<VideosVO> queryMyLikeVideos(@Param("userId") String userId);

    /**
     * 查询g关注视频
     * @param userId
     * @return
     */
    public List<VideosVO> queryMyFollowVideos(@Param("userId") String userId);

    /**
     * 对视频喜欢的数量进行累加
     * @param videoId
     */
    public void addVideoLikeCount(String videoId);

    /**
     * 对视频喜欢的数量进行累减
     * @param videoId
     */
    public void reduceVideoLikeCount(String videoId);
}
