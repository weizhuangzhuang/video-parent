package com.wzz.video.controller;

import com.wzz.video.pojo.Users;
import com.wzz.video.pojo.vo.UsersVO;
import com.wzz.video.service.UserService;
import com.wzz.video.utils.MD5Utils;
import com.wzz.video.utils.VideoJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录的controller"})
public class RegistLoginController extends BasicController{

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册" , notes = "用户注册的接口")
    @PostMapping("/regist")
    public VideoJSONResult regist(@RequestBody Users user) throws Exception {
        //1.判断用户名和密码是否为空
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return VideoJSONResult.errorMsg("用户名和密码不能为空");
        }
        //2.判断用户名是否存在
        if(!userService.queryUserNameIsExist(user.getUsername())){
            //如果没有查到就新增
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            user.setFollowCounts(0);
            userService.saveUser(user);
        } else {
            return VideoJSONResult.errorMsg("用户已存在");
        }
        //3.保存用户，注册信息
        user.setPassword("");
        return VideoJSONResult.ok(setUserSession(user));
    }



    @ApiOperation(value = "用户登录" , notes = "用户登录的接口")
    @PostMapping("/login")
    public VideoJSONResult login(@RequestBody Users user) throws Exception {
        boolean flag = userService.queryPasswordByUserName(user.getUsername(),MD5Utils.getMD5Str(user.getPassword()));
        if(StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())){
            return VideoJSONResult.errorMsg("用户名和密码不能为空");
        }
        if(!userService.queryUserNameIsExist(user.getUsername())){
            return VideoJSONResult.errorMsg("用户不存在");
        }else if(!flag){
            return VideoJSONResult.errorMsg("密码错误");
        }

        Users userResult = userService.queryUserByuserName(user.getUsername());
        userResult.setPassword("");
        return VideoJSONResult.ok(setUserSession(userResult));
    }

    @ApiOperation(value = "用户注销" , notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String" ,paramType = "query")
    @PostMapping("/logout")
    public VideoJSONResult logout(String userId){
        redisOperator.del(USER_REDIS_SESSION + ":" + userId);
        return VideoJSONResult.ok();
    }

    /**
     * 设置users的Session
     * @param userModel
     * @return
     */
    public UsersVO setUserSession(Users userModel){
        String uniqueToken = UUID.randomUUID().toString();
        redisOperator.set(USER_REDIS_SESSION + ":" + userModel.getId() , uniqueToken , 1000 * 60 * 30);
        UsersVO usersVO = new UsersVO();
        usersVO.setUserToken(uniqueToken);
        //将user中的属性拷贝到usersVO中去
        BeanUtils.copyProperties(userModel,usersVO);
        return usersVO;
    }
}
