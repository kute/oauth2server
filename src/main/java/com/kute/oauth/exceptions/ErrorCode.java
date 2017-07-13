package com.kute.oauth.exceptions;

/**
 * Created by longbai on 2017/7/12.
 */
public enum ErrorCode {

    DEFAULT_ERROR(40000, "Bad Request"),
    WRONG_CLIENTID(40001, "Wrong Clientid"),
    WRONG_CLIENT_SECRET(40002, "Wrong Client Secret");

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
