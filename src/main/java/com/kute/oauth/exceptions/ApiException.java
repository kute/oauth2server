package com.kute.oauth.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by longbai on 2017/7/12.
 */
public class ApiException extends RuntimeException{

    private ErrorCode code = ErrorCode.DEFAULT_ERROR;

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    public ApiException() {
        super();
    }

    public ApiException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ApiException(ErrorCode code, HttpStatus httpStatus) {
        super(code.getMessage());
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public ApiException(ErrorCode code, HttpStatus httpStatus, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
