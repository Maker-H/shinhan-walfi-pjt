package com.shinhan.walfi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Getter
@AllArgsConstructor
public enum BattleErrorCode {

    NOT_A_USER_ACCOUNT(BAD_REQUEST, "유저 정보와 대표계좌가 일치하지 않습니다"),
    NOT_ENOUGH_BATTLE(BAD_REQUEST, "전투횟수가 부족합니다"),
    NO_TOP_TEN(BAD_REQUEST, "랭킹 안의 사용자가 아닙니다");

    private final HttpStatus httpStatus;

    private final String message;
}
