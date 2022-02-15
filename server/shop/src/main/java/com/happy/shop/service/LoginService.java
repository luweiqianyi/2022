package com.happy.shop.service;

import com.happy.shop.dao.LoginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    LoginDao loginDao;

    public boolean login(String userId,String userName){
        return loginDao.login(userId,userName);
    }
}
