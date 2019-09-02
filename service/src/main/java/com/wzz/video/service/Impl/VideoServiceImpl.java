package com.wzz.video.service.Impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzz.video.mapper.VideosMapper;
import com.wzz.video.mapper.VideosMapperCustom;
import com.wzz.video.pojo.Videos;
import com.wzz.video.pojo.vo.VideosVO;
import com.wzz.video.service.VideoService;
import com.wzz.video.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private VideosMapper videosMapper;

    //注入自定义mapper
    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private Sid sid;


    /**
     * 保存视频信息到数据库
     * @param videos
     */
    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public String saveVideos(Videos videos) {

        String id = sid.nextShort();
        videos.setId(id);
        //保存一个实体，null的属性不会保存，会使用数据库默认值
        videosMapper.insertSelective(videos);

        return id;
    }

    /**
     * 此方法用来更新视频封面信息
     * @param videoId
     * @param coverPath
     */
    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void updateVideos(String videoId , String coverPath) {
        Videos videos = new Videos();
        videos.setId(videoId);
        videos.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(videos);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Integer page, Integer pageSize) {
        PageHelper.startPage(page , pageSize);
        List<VideosVO> videosVO = videosMapperCustom.queryAllVideos();
        PageInfo<VideosVO> pageList = new PageInfo<>(videosVO);

        PagedResult pagedResult = new PagedResult();
        //查几页
        pagedResult.setPage(page);
        //总记录数
        pagedResult.setRecords(pageList.getTotal());
        //总页数
        pagedResult.setTotal(pageList.getPages());
        //每一条结果集
        pagedResult.setRows(videosVO);

        return pagedResult;
    }
}
