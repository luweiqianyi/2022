package com.happy.shop.common;

import lombok.Getter;
import lombok.Setter;

public class ResultDescription {
    public enum ResultCode{
        LOGIN_SUCCESS(100),
        LOGIN_FAIL(101);

        @Getter
        private int code;

        ResultCode(int code){
            this.code = code;
        }
    }
    /**
     * 返回码
     */
    @Getter
    @Setter
    private int code;

    /**
     * 描述信息
     */
    @Getter
    @Setter
    private String description;

    public ResultDescription(){}

    public ResultDescription(int code,String description){
        this.code = code;
        this.description = description;
    }
}
