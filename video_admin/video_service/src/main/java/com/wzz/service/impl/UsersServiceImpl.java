package com.wzz.service.impl;

import org.apache.commons.lang3.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wzz.mapper.UsersMapper;
import com.wzz.pojo.Users;
import com.wzz.pojo.UsersExample;
import com.wzz.pojo.UsersExample.Criteria;
import com.wzz.service.UsersService;
import com.wzz.utils.PagedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public PagedResult queryUsersList(Users user , Integer page, Integer pageSize) {

        String username = "";
        String nickname = "";

        if(user != null){
            username = user.getUsername();
            nickname = user.getNickname();
        }

        PageHelper.startPage(page , pageSize);
        UsersExample usersExample = new UsersExample();
        Criteria criteria = usersExample.createCriteria();
        if(StringUtils.isNotBlank(username)){
            criteria.andUsernameLike("%" + username + "%");
        }
        if(StringUtils.isNotBlank(nickname)){
            criteria.andNicknameLike("%" + nickname + "%");
        }
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        PageInfo<Users> pageInfo = new PageInfo<>(usersList);

        PagedResult pagedResult = new PagedResult();
        //获取总页数
        pagedResult.setTotal(pageInfo.getPages());
        //结果集
        pagedResult.setRows(usersList);
        //获取总记录数
        pagedResult.setRecords(pageInfo.getTotal());

        pagedResult.setPage(page);

        return pagedResult;
    }
}
