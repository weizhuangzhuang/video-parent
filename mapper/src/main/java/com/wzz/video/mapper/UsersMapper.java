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

    /**
     * 增加粉丝数量
     * @param userId
     */
    public void addFansCount(String userId);

    /**
     * 增加关注数量
     * @param userId
     */
    public void addFollersCount(String userId);

    /**
     * 减少粉丝数量
     * @param userId
     */
    public void reduceFansCount(String userId);

    /**
     * 增加关注数量
     * @param userId
     */
    public void reduceFollersCount(String userId);

}