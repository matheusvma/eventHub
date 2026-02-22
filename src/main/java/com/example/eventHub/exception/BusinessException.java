package com.example.eventHub.exception;

/**
 * Exceção base para erros de negócio
 */
public class BusinessException extends RuntimeException {

    private final String code;
    private final int httpStatus;

    public BusinessException(String message, String code, int httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, String code, int httpStatus, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}

