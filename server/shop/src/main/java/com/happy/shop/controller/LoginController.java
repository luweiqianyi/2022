package com.happy.shop.controller;

import com.happy.shop.common.ResultDescription;
import com.happy.shop.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    LoginService loginService;

    @Autowired
    LoginController(LoginService service){
        this.loginService = service;
    }
    /**
     * post方式的登录请求，请求路径形如：http://127.0.0.1:8082/shop/login
     * userId=nicklaus  userPwd=666888
     * 可以用postman来构造请求，这样就不需要写繁杂的前端页面代码了
     * 
     * @param userId 用户ID,和客户端协商好的请求参数名字
     * @param userPwd 用户密码，和客户端协商好的请求参数名字
     * @return Springboot框架默认的json数据返回格式，形如
     * {
     *     "code": 100,
     *     "description": "用户登录成功"
     * } 注意：此时http返回的是200
     * 若客户端请求时请求参数写错，比如将userPwd写成userPassword，服务器是不认识的，会返回以下报错结果
     * {
     *     "timestamp": "2022-02-16T03:17:44.989+00:00",
     *     "status": 400,
     *     "error": "Bad Request",
     *     "path": "/shop/login"
     * }
     */
    @PostMapping("login")
    public ResultDescription login(@RequestParam String userId,@RequestParam String userPwd){
        boolean bLoginSuccess = loginService.login(userId,userPwd);
        ResultDescription resultDescription;
        if(bLoginSuccess){
            resultDescription = new ResultDescription(
                    ResultDescription.ResultCode.LOGIN_SUCCESS.getCode(),
                    "用户登录成功");
        }
        else {
            resultDescription = new ResultDescription(
                    ResultDescription.ResultCode.LOGIN_FAIL.getCode(),
                    "用户登录失败");
        }
        return resultDescription;
    }
}
