package com.wzz.service;

import com.wzz.pojo.Users;
import com.wzz.utils.PagedResult;

public interface UsersService {

    /**
     * 分页查询用户列表
     * @param page
     * @param pageSize
     * @return
     */
    public PagedResult queryUsersList(Users user , Integer page , Integer pageSize);

}
