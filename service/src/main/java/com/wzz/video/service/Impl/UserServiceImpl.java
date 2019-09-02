package com.wzz.video.service.Impl;


import com.wzz.video.mapper.UsersMapper;
import com.wzz.video.pojo.Users;
import com.wzz.video.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String username) {
        Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user);
        return result == null ? false : true;
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {

        String userId = sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public boolean queryPasswordByUserName(String username , String password) {
        Users user = new Users();
        user.setUsername(username);
        Users result = usersMapper.selectOne(user);
        //判断字符串相等怎么能用==？？？
        return StringUtils.equals(result.getPassword(),password) ? true : false;
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public Users queryUserByuserName(String username) {
        Users user = new Users();
        user.setUsername(username);
        return usersMapper.selectOne(user);

        /**
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username" , username);
        criteria.andEqualTo("password" , password);
        Users result = usersMapper.selectOneByExample(criteria);
        return result;
         **/
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void updataUserInfo(Users users) {
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id" , users.getId());
        usersMapper.updateByExampleSelective(users,userExample);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public Users queryUserInfo(String userId) {
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id" ,userId);
        return usersMapper.selectOneByExample(userExample);
    }
}
