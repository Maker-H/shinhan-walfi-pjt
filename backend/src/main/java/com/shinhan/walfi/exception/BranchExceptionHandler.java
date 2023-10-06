package com.shinhan.walfi.exception;

import com.shinhan.walfi.domain.HttpResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.shinhan.walfi.domain.HttpResult.Result.FAIL;

@Slf4j
@RestControllerAdvice
public class BranchExceptionHandler {

    private ResponseEntity<HttpResult> convertFailCodeToHttpResponse(BranchErrorCode branchErrorCode) {

        HttpResult result = new HttpResult(
                branchErrorCode.getHttpStatus(),
                FAIL,
                branchErrorCode.name()
        );

        result.setData(branchErrorCode.getMessage());

        return ResponseEntity.status(result.getStatus()).body(result);
    }

    @ExceptionHandler(BranchException.class)
    protected ResponseEntity<HttpResult> noMatchingUserException(BranchException error) {
        return convertFailCodeToHttpResponse(error.getBranchErrorCode());
    }

}
