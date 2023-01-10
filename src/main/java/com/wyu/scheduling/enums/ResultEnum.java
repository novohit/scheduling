package com.wyu.scheduling.enums;


/**
 * @author zwx
 * @date 2022/1/11-21:00
 */

public enum ResultEnum {

    ERROR(1, "服务端异常"),

    SUCCESS(0, "成功"),

    PARAM_ERROR(3, "参数错误"),

    //1000 用户相关
    ;

    Integer code;

    String desc;

    ResultEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
