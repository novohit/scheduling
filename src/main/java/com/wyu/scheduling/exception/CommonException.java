package com.wyu.scheduling.exception;

import com.wyu.scheduling.enums.ResultEnum;

/**
 * @author zwx
 * @date 2022-06-27 17:27
 */
public class CommonException extends RuntimeException {
    protected Integer code;
    protected Integer httpStatusCode = 200;

    public Integer getCode() {
        return code;
    }

    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public CommonException(){

    }

    public CommonException(Integer code, String desc) {
        super(desc);
        this.code = code;
    }

    public CommonException(Integer code, String desc, Integer httpStatusCode) {
        super(desc);
        this.code = code;
        this.httpStatusCode = httpStatusCode;
    }

    public CommonException(ResultEnum resultEnum, Integer httpStatusCode) {
        super(resultEnum.getDesc());
        this.code = resultEnum.getCode();
        this.httpStatusCode = httpStatusCode;
    }

    public CommonException(ResultEnum resultEnum) {
        super(resultEnum.getDesc());
        this.code = resultEnum.getCode();
    }

    public CommonException(String msg) {
        super(msg);
        this.code = ResultEnum.ERROR.getCode();
    }
}
