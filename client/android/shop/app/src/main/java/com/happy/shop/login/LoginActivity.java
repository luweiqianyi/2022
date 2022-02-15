package com.happy.shop.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.happy.shop.MainActivity;
import com.happy.shop.R;
import com.happy.shop.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();
    }

    void initialization(){
        initStartOtherActivity();
        initLoginBtnClickEvent();
    }

    void initStartOtherActivity(){
        Map<Integer,Class<?>> map = new HashMap<>();
        map.put(R.id.phone_login,PhoneLoginActivity.class);
        map.put(R.id.find_password,FindPasswordActivity.class);
        map.put(R.id.new_user,NewUserActivity.class);

        for (Integer key : map.keySet()){
            View view = findViewById(key);
            if(view != null){
                //view.setOnClickListener(new View.OnClickListener(){
                view.setOnClickListener(v -> {
                    Intent intent = new Intent(LoginActivity.this,map.get(key));
                    startActivity(intent);
                });
            }
        }
    }

    void initLoginBtnClickEvent(){
        Button btn = findViewById(R.id.loginbtn);
        EditText v1 = findViewById(R.id.userId);
        EditText v2 = findViewById(R.id.userPassword);

        final String userId = (v1==null)?"":v1.getText().toString();
        final String userPassword = (v2==null)?"":v2.getText().toString();

        if(btn != null){
            btn.setOnClickListener(v -> {

                if(!Utils.allowBtnClickAgain()){
                    return;
                }

                boolean bSuccess = ((LoginRepository)LoginRepository.getInstance()).login(userId,userPassword);

                if(bSuccess){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    ((Utils)Utils.getInstance()).showToastTip(
                            getApplicationContext(),
                            "登录失败",
                            Toast.LENGTH_SHORT,
                            true);
                }
            });
        }
    }

}