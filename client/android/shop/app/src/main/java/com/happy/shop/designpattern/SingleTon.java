package com.happy.shop.designpattern;

public class SingleTon {
    private static volatile SingleTon mInstance;

    protected SingleTon(){
    }

    public static SingleTon getInstance(){
        if(mInstance == null){
            synchronized (SingleTon.class){
                if(mInstance == null){
                    mInstance = new SingleTon();
                }
            }
        }
        return mInstance;
    }
}
