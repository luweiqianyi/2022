package com.happy.shop.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginNet {
    private String url;

    private static OkHttpClient client = new OkHttpClient();

    public LoginNet(){

    }

    public LoginNet(String url){
        this.url = url;
    }

    public void setLoginURL(String url){
        this.url = url;
    }

    /**
     * 网络登录功能
     * 和服务器协商好的字段是“userId”和“userPwd”
     * @param userId 用户Id
     * @param userPassword 用户密码
     */
    public boolean login(String userId,String userPassword){
        // 1. 构建请求体
        FormBody formBody = new FormBody.Builder()
                .add("userId",userId)
                .add("userPwd",userPassword)
                .build();
        // 2. 构建post请求对象
        Request request = new Request.Builder()
                .url(this.url)
                .post(formBody)
                .build();

        Response response = null;
        try {
            Call call = client.newCall(request);
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.code() == 200?true:false;
    }
}
