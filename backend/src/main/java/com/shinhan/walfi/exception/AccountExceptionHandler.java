package com.shinhan.walfi.exception;

import com.shinhan.walfi.domain.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.shinhan.walfi.domain.HttpResult.Result.FAIL;

@Slf4j
@RestControllerAdvice
public class AccountExceptionHandler {

    private ResponseEntity<HttpResult> convertFailCodeToHttpResponse(AccountErrorCode accountErrorCode) {

        HttpResult result = new HttpResult(
                accountErrorCode.getHttpStatus(),
                FAIL,
                accountErrorCode.name()
        );

        result.setData(accountErrorCode.getMessage());

        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @ExceptionHandler(AccountException.class)
    protected ResponseEntity<HttpResult> noMatchingUserException(AccountException error) {
        return convertFailCodeToHttpResponse(error.getAccountErrorCode());
    }

}
