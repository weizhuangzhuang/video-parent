package com.wzz.video.service.Impl;


import com.wzz.video.mapper.UsersFansMapper;
import com.wzz.video.mapper.UsersLikeVideosMapper;
import com.wzz.video.mapper.UsersMapper;
import com.wzz.video.mapper.UsersReportMapper;
import com.wzz.video.pojo.Users;
import com.wzz.video.pojo.UsersFans;
import com.wzz.video.pojo.UsersLikeVideos;
import com.wzz.video.pojo.UsersReport;
import com.wzz.video.service.UserService;
import org.apache.commons.lang3.StringUtils;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private UsersFansMapper usersFansMapper;

    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

    @Autowired
    private UsersReportMapper usersReportMapper;

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

    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example userExample = new Example(Users.class);
        Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("id" ,userId);
        return usersMapper.selectOneByExample(userExample);
    }

    @Transactional(propagation =  Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(videoId)){
            return false;
        }
        Example example = new Example(UsersLikeVideos.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId" ,userId);
        criteria.andEqualTo("videoId" ,videoId);

        List<UsersLikeVideos> list = usersLikeVideosMapper.selectByExample(example);

        if(list != null && list.size() > 0){
            return true;
        }
        return false;
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void saveUserFanRelation(String userId, String fanId) {
        //1.添加用户与粉丝之间的关机
        String id = sid.nextShort();
        UsersFans usersFans = new UsersFans();
        usersFans.setId(id);
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);

        usersFansMapper.insert(usersFans);

        //2.增加用户的粉丝数量
        usersMapper.addFansCount(userId);

        //3.增加粉丝的关注数量
        usersMapper.addFollersCount(fanId);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void deleteUserFanRelation(String userId, String fanId) {
        //1.删除用户与粉丝之间的关机
        Example example = new Example(UsersFans.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("userId" , userId);
        criteria.andEqualTo("fanId" , fanId);

        usersFansMapper.deleteByExample(example);

        //2.减少用户的粉丝数量
        usersMapper.reduceFansCount(userId);

        //3.减少粉丝的关注数量
        usersMapper.reduceFollersCount(fanId);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    @Override
    public void reportUser(UsersReport usersReport) {
        String id = sid.nextShort();
        usersReport.setId(id);
        usersReport.setCreateDate(new Date());

        usersReportMapper.insert(usersReport);
    }
}
