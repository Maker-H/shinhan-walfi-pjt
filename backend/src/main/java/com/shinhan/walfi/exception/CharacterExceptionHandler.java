package com.shinhan.walfi.exception;

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

@Slf4j
@RestControllerAdvice
public class CharacterExceptionHandler {

    private ResponseEntity<HttpResult> convertFailCodeToHttpResponse(CharacterErrorCode characterErrorCode) {

        HttpResult result = new HttpResult(
                characterErrorCode.getHttpStatus(),
                FAIL,
                characterErrorCode.name()
        );

        result.setData(characterErrorCode.getMessage());

        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @ExceptionHandler(CharacterException.class)
    protected ResponseEntity<HttpResult> noMatchingUserException(CharacterException error) {
        return convertFailCodeToHttpResponse(error.getCharacterErrorCode());
    }

}
