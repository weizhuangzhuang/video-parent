package com.wzz.video.mapper;

import com.wzz.video.pojo.Users;
import com.wzz.video.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {

    /**
     * 用户喜欢数累加
     * @param userId
     */
    public void addReceiveLikeCount(String userId);

    /**
     * 用户喜欢数累减
     * @param userId
     */
    public void reduceReceiveLikeCount(String userId);

}