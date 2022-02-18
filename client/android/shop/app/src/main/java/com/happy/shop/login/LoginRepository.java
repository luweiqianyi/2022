package com.happy.shop.login;

import com.happy.shop.net.LoginNet;

public class LoginRepository {

    private static volatile LoginRepository mInstance;

    private LoginRepository(){
    }

    public static LoginRepository getInstance(){
        if(mInstance == null){
            synchronized (LoginRepository.class){
                if(mInstance == null){
                    mInstance = new LoginRepository();
                }
            }
        }
        return mInstance;
    }

    /**
     * 用户登录功能
     * @param userId 登录时用户的唯一ID
     * @param userPassword 用户密码
     * @return
     */
    // TODO 待提升: 1.https的登录处理 2.密码加密处理
    public boolean login(String userId,String userPassword){
        LoginNet loginNet = new LoginNet("http://192.168.36.122:8082/shop/login");
        return loginNet.login(userId,userPassword);
    }
}
