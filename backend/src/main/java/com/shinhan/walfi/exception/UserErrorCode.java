package com.shinhan.walfi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Getter
@AllArgsConstructor
public enum UserErrorCode {

    NO_MATCHING_USER(BAD_REQUEST, "해당 유저를 조회할 수 없거나 틀린 비밀번호 입니다"),

    ID_DUPLICATED(BAD_REQUEST, "중복된 아이디 입니다.");

    private final HttpStatus httpStatus;

    private final String message;
}
