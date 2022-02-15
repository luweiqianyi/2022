package com.happy.shop.login;

import com.happy.shop.designpattern.SingleTon;
import com.happy.shop.net.LoginNet;

public class LoginRepository extends SingleTon {

    public boolean login(String userId,String userPassword){
//        if("Zhangsan".equals(userId) && "888666".equals(userPassword)){
//            return true;
//        }
//        return false;

        LoginNet loginNet = new LoginNet("http://127.0.0.1:8080");
        return loginNet.login(userId,userPassword);
    }
}
