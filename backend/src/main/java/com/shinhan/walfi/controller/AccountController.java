package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.banking.AccountReqDto;
import com.shinhan.walfi.dto.banking.AccountResDto;
import com.shinhan.walfi.service.banking.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ApiOperation(value = "대표 계좌로 사용자가 가진 실질 계좌들을 조회")
    public ResponseEntity<HttpResult> getAccounts(@ApiIgnore @AuthenticationPrincipal User user, @RequestBody AccountReqDto accountReqDto) {
        String userId = user.getUserId();
        AccountResDto accountResDto = accountService.getAccounts(userId, accountReqDto.getUserMainAccount());

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(accountResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
