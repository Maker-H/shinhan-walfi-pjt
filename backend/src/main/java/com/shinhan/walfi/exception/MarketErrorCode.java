package com.shinhan.walfi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum MarketErrorCode {
    LACK_OF_POINT(BAD_REQUEST, "포인트가 부족합니다.");

    private final HttpStatus httpStatus;

    private final String message;

}
