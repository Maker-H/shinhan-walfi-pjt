package com.shinhan.walfi.controller;


import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.banking.AccountReqDto;
import com.shinhan.walfi.dto.banking.AccountResDto;
import com.shinhan.walfi.dto.banking.GasResDto;
import com.shinhan.walfi.service.banking.CryptoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/crypto")
@RequiredArgsConstructor
public class CryptoController {
    private final CryptoService cryptoService;

    @GetMapping("/gas")
    @ApiOperation(value = "송금시 필요한 가스비 조회")
    public ResponseEntity<HttpResult> getGasInfo() {
        GasResDto gasResDto = cryptoService.getGasFee();

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(gasResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    //TODO: 송금기능


}
