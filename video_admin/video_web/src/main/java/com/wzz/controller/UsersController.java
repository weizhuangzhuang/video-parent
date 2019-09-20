package com.wzz.controller;


import com.wzz.bean.AdminUser;
import com.wzz.pojo.Users;
import com.wzz.service.UsersService;
import com.wzz.utils.PagedResult;
import com.wzz.utils.VideoJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("login")
    @ResponseBody
    public VideoJSONResult userLogin(String username , String password ,
                                     HttpServletRequest request , HttpServletResponse response){
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return VideoJSONResult.errorMsg("用户名或者密码不能为空");
        } else if(username.equals("lee") && password.equals("lee")){
            String token = UUID.randomUUID().toString();
            AdminUser user = new AdminUser(username , password ,token);
            request.getSession().setAttribute("sessionUser" , user);
            return VideoJSONResult.ok();
        }

        return VideoJSONResult.errorMsg("登录失败，请重试");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("sessionUser");
        return "login";
    }


    @GetMapping("/showList")
    public String showList() {
        return "users/usersList";
    }

    @PostMapping("/list")
    @ResponseBody
    public PagedResult queryBgmList(Users user , Integer page) {
        return usersService.queryUsersList(user , page, 10);
    }

}
