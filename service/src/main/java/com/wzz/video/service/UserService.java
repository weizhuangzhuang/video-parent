package com.wzz.video.service;

import com.wzz.video.pojo.Users;

public interface UserService {

    /**
     * 查询用户是否存在
     * @param username
     * @return
     */
    public boolean queryUserNameIsExist(String username);

    /**
     * 新增用户
     * @param user
     */
    public void saveUser(Users user) throws Exception;

    /**
     * 通过用户名查询密码
     * @param username
     * @return
     */
    public boolean queryPasswordByUserName(String username,String password);

    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    public Users queryUserByuserName(String username);

    /**
     * 更新用户信息
     * @param users
     */
    public void updataUserInfo(Users users);

    /**
     * 通过用户ID查询用户信息
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);

    /**
     * 查询当前登录者和视频的点赞关系
     * @param loginUserId
     * @param videoId
     * @return
     */
    public boolean isUserLikeVideo(String loginUserId , String videoId);
}
