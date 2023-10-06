package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.banking.ExchangeResDto;
import com.shinhan.walfi.dto.banking.fromGlobalExchangeReqDto;
import com.shinhan.walfi.dto.banking.toGlobalExchangeReqDto;
import com.shinhan.walfi.service.banking.ExchangeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("/info")
    @ApiOperation(value = "오늘의 나라별 환율과 등락률을 조회")
    public ResponseEntity<HttpResult> getLatest() {
        //신한 api 환율 조회하기
        HttpResult res;

        try {
            res = HttpResult.getSuccess();
            ExchangeResDto dto = exchangeService.getLatest();
            res.setData(dto);
        } catch (ParseException e) {
            res = new HttpResult(HttpStatus.FORBIDDEN, HttpResult.Result.ERROR, "환율 조회 실패");
        }

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/toglobal")
    @ApiOperation(value = "원화 -> 외화로 환전")
    public ResponseEntity<HttpResult> toGlobalExchange(@ApiIgnore @AuthenticationPrincipal User user, @RequestBody toGlobalExchangeReqDto toGlobalExchangeReqDto) {

        exchangeService.toGlobalExchange(user.getUserId(),
                toGlobalExchangeReqDto.get사용자대표계좌(),
                toGlobalExchangeReqDto.get도착계좌통화코드(),
                toGlobalExchangeReqDto.get금액(),
                toGlobalExchangeReqDto.get전신환매도환율());

        HttpResult res = HttpResult.getSuccess();

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/fromglobal")
    @ApiOperation(value = "외화 -> 원화로 환전")
    public ResponseEntity<HttpResult> fromGlobalExchange(@ApiIgnore @AuthenticationPrincipal User user,@RequestBody fromGlobalExchangeReqDto fromGlobalExchangeReqDto) {

        exchangeService.fromGlobalExchange(user.getUserId(),
                fromGlobalExchangeReqDto.get사용자대표계좌(),
                fromGlobalExchangeReqDto.get출발계좌통화코드(),
                fromGlobalExchangeReqDto.get금액(),
                fromGlobalExchangeReqDto.get전신환매입환율());

        HttpResult res = HttpResult.getSuccess();

        return ResponseEntity.status(res.getStatus()).body(res);
    }
}