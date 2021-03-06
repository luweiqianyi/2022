package com.happy.shop.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        if(btn != null){
            btn.setOnClickListener(v -> {

                if(!Utils.allowBtnClickAgain()){
                    return;
                }

                Utils.getInstance().showOrHideSoftKeyboard(this,false);

                EditText v1 = findViewById(R.id.userId);
                EditText v2 = findViewById(R.id.userPassword);

                String userId = (v1==null)?"":v1.getText().toString();
                String userPassword = (v2==null)?"":v2.getText().toString();

                boolean bSuccess = LoginRepository.getInstance().login(userId,userPassword);

                if(bSuccess){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Utils.getInstance().showToastTip(
                            LoginActivity.this,//getApplicationContext(),
                            "????????????",
                            Toast.LENGTH_SHORT,
                            true);
                }
            });
        }
    }

}