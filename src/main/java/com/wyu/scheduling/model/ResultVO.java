package com.wyu.scheduling.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wyu.scheduling.enums.ResultEnum;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * @author zwx
 * @date 2023-01-10 14:31
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)//如果json的data为null 不返回给前端
public class ResultVO<T> {
    private Integer code;

    private  String msg;

    private T data;


    private ResultVO() {
    }

    public ResultVO(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<T>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getDesc(), data);
    }

    public static <T> ResultVO<T> success(String msg) {
        return new ResultVO<T>(ResultEnum.SUCCESS.getCode(), msg, null);
    }


    public static <T> ResultVO<T> error(String msg) {
        return new ResultVO<>(ResultEnum.ERROR.getCode(), msg, null);
    }

    public static <T> ResultVO<T> error(Integer code, String msg, T data) {
        return new ResultVO<>(code, msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
