package com.shinhan.walfi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserException extends RuntimeException {

    UserErrorCode userErrorCode;

}
