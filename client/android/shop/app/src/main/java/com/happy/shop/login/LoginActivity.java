package com.happy.shop.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.happy.shop.R;
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

    }

}