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

    public boolean login(String userId,String userPassword){
//        if("Zhangsan".equals(userId) && "888666".equals(userPassword)){
//            return true;
//        }
//        return false;

        LoginNet loginNet = new LoginNet("http://127.0.0.1:8082");
        return loginNet.login(userId,userPassword);
    }
}
