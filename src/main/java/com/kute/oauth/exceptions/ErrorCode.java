package com.kute.oauth.exceptions;

/**
 * Created by longbai on 2017/7/12.
 */
public enum ErrorCode {

    DEFAULT_ERROR(40000, "Bad request"),
    WRONG_CLIENTID(40001, "wrong clientid");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
