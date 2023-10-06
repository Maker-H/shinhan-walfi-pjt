package com.shinhan.walfi.exception;

import com.shinhan.walfi.domain.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.shinhan.walfi.domain.HttpResult.Result.FAIL;

@Slf4j
@RestControllerAdvice
public class MarketExceptionHandler {

    private ResponseEntity<HttpResult> convertFailCodeToHttpResponse(MarketErrorCode marketErrorCode) {

        HttpResult result = new HttpResult(
                marketErrorCode.getHttpStatus(),
                FAIL,
                marketErrorCode.name()
        );

        result.setData(marketErrorCode.getMessage());

        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @ExceptionHandler(MarketException.class)
    protected ResponseEntity<HttpResult> noMatchingUserException(MarketException error) {
        return convertFailCodeToHttpResponse(error.getMarketErrorCode());
    }

}