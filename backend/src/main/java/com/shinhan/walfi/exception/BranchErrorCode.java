package com.shinhan.walfi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Getter
@AllArgsConstructor
public enum BranchErrorCode {

    NO_MATCHING_BRANCH(BAD_REQUEST, "해당 지점 정보가 존재하지 않습니다"),
    NOT_BRANCH_MANAGER(BAD_REQUEST, "해당 사용자는 지점장이 아닙니다");

    private final HttpStatus httpStatus;

    private final String message;
}
