package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.dto.banking.AccountReqDto;
import com.shinhan.walfi.dto.banking.AccountResDto;
import com.shinhan.walfi.dto.banking.ProductReqDto;
import com.shinhan.walfi.service.banking.AccountService;
import com.shinhan.walfi.service.banking.ProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.SimpleTimeZone;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ApiOperation(value = "정기예금 생성", tags = "상품명 - levelup정기예금, 만기일형식 - 230101, 금리형식 - 0.00")
    public ResponseEntity<HttpResult> createTimeDeposit(ProductReqDto productReqDto){

        String userId = productReqDto.getUserId();
        String 통화코드 = productReqDto.get통화코드();
        String 상품명 = productReqDto.get상품명();
        String 만기일 = productReqDto.get만기일();
        BigDecimal 금리 = BigDecimal.valueOf(Double.parseDouble(productReqDto.get금리()));

        productService.createTimeDeposit(userId, 통화코드, 상품명, 만기일, 금리);

        HttpResult res;
        res = HttpResult.getSuccess();

        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
