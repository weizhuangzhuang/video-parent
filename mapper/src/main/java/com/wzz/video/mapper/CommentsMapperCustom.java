package com.wzz.video.mapper;

import com.wzz.video.pojo.Comments;
import com.wzz.video.pojo.vo.CommentsVO;
import com.wzz.video.utils.MyMapper;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Comments> {

    public List<CommentsVO> queryComments(String videoId);

}