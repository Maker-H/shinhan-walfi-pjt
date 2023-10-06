package com.shinhan.walfi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Getter
@AllArgsConstructor
public enum TransferErrorCode {

    NOT_FOUND_DEPOSIT_MAIN_ACCOUNT_NUMBER(BAD_REQUEST, "입금 대표 계좌 번호를 조회할 수 없습니다."),
    NOT_FOUND_WITHDRAWAL_MAIN_ACCOUNT_NUMBER(BAD_REQUEST, "출금 대표 계좌 번호를 조회할 수 없습니다."),
    NOT_FOUND_DEPOSIT_SUB_ACCOUNT_NUMBER(BAD_REQUEST, "입금 세부 계좌 번호를 조회할 수 없습니다."),
    NOT_FOUND_WITHDRAWAL_SUB_ACCOUNT_NUMBER(BAD_REQUEST, "출금 세부 계좌 번호를 조회할 수 없습니다."),
    OVERDRAWN(BAD_REQUEST, "잔액이 부족합니다."),
    NOT_FOUND_KRW_ACCOUNT(BAD_REQUEST, "원화 계좌를 조회 할 수 없습니다"),
    NOT_FOUND_GLOBAL_ACCOUNT(BAD_REQUEST, "외화 계좌를 조회 할 수 없습니다"),
    NOT_USERS_MAIN_ACCOUNT(BAD_REQUEST, "사용자의 대표 계좌가 아닙니다"),
    NOT_FOR_BUY(BAD_REQUEST, "해당 통화 코드는 도착 계좌를 위한 통화 코드입니다 원화 -> 외화"),
    NOT_FOR_SELL(BAD_REQUEST, "해당 통화 코드는 출발 계좌를 위한 통화 코드입니다 외화 -> 원화 "),
    NOT_FOUND_CURRENCY(BAD_REQUEST, "정확한 통화 코드를 입력해주세요");

    private final HttpStatus httpStatus;

    private final String message;
}
