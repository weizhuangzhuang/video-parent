package com.wzz.video.service.Impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzz.video.mapper.*;
import com.wzz.video.pojo.Comments;
import com.wzz.video.pojo.SearchRecords;
import com.wzz.video.pojo.UsersLikeVideos;
import com.wzz.video.pojo.Videos;
import com.wzz.video.pojo.vo.CommentsVO;
import com.wzz.video.pojo.vo.VideosVO;
import com.wzz.video.service.VideoService;
import com.wzz.video.utils.PagedResult;
import com.wzz.video.utils.TimeAgoUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService{

    @Autowired
    private VideosMapper videosMapper;

    //注入自定义mapper
    @Autowired
    private VideosMapperCustom videosMapperCustom;

    @Autowired
    private SearchRecordsMapper searchRecordsMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private CommentsMapperCustom commentsMapperCustom;

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
    public PagedResult getAllVideos(Videos videos , Integer isSaveRecords , Integer page , Integer pageSize) {

        //保存搜索记录
        String desc = videos.getVideoDesc();
        String userId = videos.getUserId();
        if(isSaveRecords != null && isSaveRecords == 1){
            SearchRecords searchRecords = new SearchRecords();
            searchRecords.setId(sid.nextShort());
            searchRecords.setContent(desc);
            searchRecordsMapper.insert(searchRecords);
        }

        PageHelper.startPage(page , pageSize);
        List<VideosVO> videosVO = videosMapperCustom.queryAllVideos(desc , userId);
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

    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {

        PageHelper.startPage(page , pageSize);
        List<VideosVO> list = videosMapperCustom.queryMyLikeVideos(userId);
        PageInfo<VideosVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        //查几页
        pagedResult.setPage(page);
        //总记录数
        pagedResult.setRecords(pageList.getTotal());
        //总页数
        pagedResult.setTotal(pageList.getPages());
        //每一条结果集
        pagedResult.setRows(list);

        return pagedResult;
    }

    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {

        PageHelper.startPage(page , pageSize);
        List<VideosVO> list = videosMapperCustom.queryMyFollowVideos(userId);
        PageInfo<VideosVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        //查几页
        pagedResult.setPage(page);
        //总记录数
        pagedResult.setRecords(pageList.getTotal());
        //总页数
        pagedResult.setTotal(pageList.getPages());
        //每一条结果集
        pagedResult.setRows(list);

        return pagedResult;
    }

    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public List<String> getHotWords() {
        return searchRecordsMapper.getHotWords();
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
        //1.保存用户和视频的喜欢点赞关联关系表
        String likeId = sid.nextShort();
        UsersLikeVideos usersLikeVideos = new UsersLikeVideos();
        usersLikeVideos.setId(likeId);
        usersLikeVideos.setUserId(userId);
        usersLikeVideos.setVideoId(videoId);

        usersLikeVideosMapper.insert(usersLikeVideos);

        //2.视频喜欢数量增加
        videosMapperCustom.addVideoLikeCount(videoId);
        //3.用户受喜欢数量增加
        usersMapper.addReceiveLikeCount(videoCreaterId);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {

        // 1. 删除用户和视频的喜欢点赞关联关系表

        Example example = new Example(UsersLikeVideos.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("videoId", videoId);

        usersLikeVideosMapper.deleteByExample(example);

        //2.视频喜欢数量减少
        videosMapperCustom.reduceVideoLikeCount(videoId);
        //3.用户受喜欢数量减少
        usersMapper.reduceReceiveLikeCount(videoCreaterId);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void saveComments(Comments comments) {
        String cid = sid.nextShort();
        comments.setId(cid);
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
    }
    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
        PageHelper.startPage(page , pageSize);
        Example example = new Example(Comments.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("videoId" ,videoId);
        List<CommentsVO> comments = commentsMapperCustom.queryComments(videoId);

        for (CommentsVO c : comments) {
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }
        PageInfo<CommentsVO> pageList = new PageInfo<>(comments);

        PagedResult pagedResult = new PagedResult();
        //查几页
        pagedResult.setPage(page);
        //总记录数
        pagedResult.setRecords(pageList.getTotal());
        //总页数
        pagedResult.setTotal(pageList.getPages());
        //每一条结果集
        pagedResult.setRows(comments);

        return pagedResult;


    }
}
