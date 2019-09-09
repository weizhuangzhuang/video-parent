package com.wzz.video.mapper;

import com.wzz.video.pojo.Videos;
import com.wzz.video.pojo.vo.VideosVO;
import com.wzz.video.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc);

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
