package com.shinhan.walfi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.shinhan.walfi.domain.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.shinhan.walfi.domain.HttpResult.Result.ERROR;
import static com.shinhan.walfi.domain.HttpResult.Result.FAIL;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class UserExceptionHandler {

    private ResponseEntity<HttpResult> convertFailCodeToHttpResponse(UserErrorCode userErrorCode) {

        HttpResult result = new HttpResult(
                userErrorCode.getHttpStatus(),
                FAIL,
                userErrorCode.name()
        );

        result.setData(userErrorCode.getMessage());

        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @ExceptionHandler(UserException.class)
    protected ResponseEntity<HttpResult> noMatchingUserException(UserException error) {
        return convertFailCodeToHttpResponse(error.getUserErrorCode());
    }

}
