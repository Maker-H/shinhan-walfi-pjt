package com.shinhan.walfi.exception;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class CustomJWTException extends RuntimeException{
    private final JWTErrorCode jwtErrorCode;

    public CustomJWTException(JWTErrorCode jwtErrorCode) {
        this.jwtErrorCode = jwtErrorCode;
    }
}
