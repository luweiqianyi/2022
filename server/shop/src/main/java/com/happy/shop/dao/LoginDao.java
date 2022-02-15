package com.happy.shop.dao;

import org.springframework.stereotype.Repository;

@Repository
public class LoginDao {
    public boolean login(String userId,String userName){
        if("nicklaus".equals(userId) && "666888".equals(userName)){
            return true;
        }
        else {
            return false;
        }
    }
}
