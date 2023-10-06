package com.shinhan.walfi.exception;

import com.shinhan.walfi.domain.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.shinhan.walfi.domain.HttpResult.Result.FAIL;

@Slf4j
@RestControllerAdvice
public class BattleExceptionHandler {

    private ResponseEntity<HttpResult> convertFailCodeToHttpResponse(BattleErrorCode battleErrorCode) {

        HttpResult result = new HttpResult(
                battleErrorCode.getHttpStatus(),
                FAIL,
                battleErrorCode.name()
        );

        result.setData(battleErrorCode.getMessage());

        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @ExceptionHandler(BattleException.class)
    protected ResponseEntity<HttpResult> noMatchingUserException(BattleException error) {
        return convertFailCodeToHttpResponse(error.getBattleErrorCode());
    }

}
