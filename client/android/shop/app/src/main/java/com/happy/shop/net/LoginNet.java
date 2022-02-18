package com.happy.shop.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

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
    // TODO 待优化：对于 e.printStackTrace();的处理
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

        final Response[] response = {null};
        Call call = client.newCall(request);

        // 主线程(UI线程)和子线程都要访问这个变量
        AtomicBoolean bSuccess = new AtomicBoolean(false);

        Thread t = new Thread(() -> {
            try {
                response[0] = call.execute();

                if(response[0] == null){
                    bSuccess.set(false);
                }
                else{
                    if(response[0].code() == 200){
                        try {
                            String jsonBody = response[0].body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(jsonBody);
                                if(jsonObject.getInt("code")==100){
                                    bSuccess.set(true);
                                }
                                else {
                                    bSuccess.set(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                bSuccess.set(false);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            bSuccess.set(false);
                        }
                    }
                    else {
                        bSuccess.set(false);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();

        try {
            t.join(); // 主线程等待子线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
            bSuccess.set(false);
        }

        return bSuccess.get();
    }
}
