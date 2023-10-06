package com.shinhan.walfi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.shinhan.walfi.domain.HttpResult;
import org.hibernate.JDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.shinhan.walfi.domain.HttpResult.Result.ERROR;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@RestControllerAdvice
public class TransferExceptionHandler {

    private HttpResult convertTransferErrorCodeToHttpResult(TransferErrorCode transferErrorCode) {

        HttpResult result = new HttpResult(
                transferErrorCode.getHttpStatus(),
                ERROR,
                transferErrorCode.name()
        );

        result.setData(transferErrorCode.getMessage());

        return result;
    }

    @ExceptionHandler(TransferException.class)
    protected ResponseEntity<HttpResult> handleTransferException(TransferException error) {

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(convertTransferErrorCodeToHttpResult(error.getTransferErrorCode()));
    }

    @ExceptionHandler(JDBCConnectionException.class)
    protected ResponseEntity<String> handleConnectionException(JDBCException error) {
        return new ResponseEntity<>(BAD_GATEWAY);
    }

    @ExceptionHandler(InvalidFormatException.class)
    protected ResponseEntity<HttpResult> handleInvalidFormatException(InvalidFormatException e) {
        HttpResult httpResult = new HttpResult(BAD_REQUEST, ERROR, "파라미터 값이 잘못 전달되었습니다.");
        return ResponseEntity.status(BAD_REQUEST).body(httpResult);
    }
}
