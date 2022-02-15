package com.happy.shop.controller;

import com.happy.shop.common.ResultDescription;
import com.happy.shop.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping("login")
    public ResultDescription login(){
        return new ResultDescription();
    }
}
