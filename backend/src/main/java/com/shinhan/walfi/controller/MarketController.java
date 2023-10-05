package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.game.Goods;
import com.shinhan.walfi.dto.game.BuyReqDto;
import com.shinhan.walfi.dto.game.GoodsCharacterResDto;
import com.shinhan.walfi.dto.game.GoodsItemResDto;
import com.shinhan.walfi.dto.game.MarketReqDto;
import com.shinhan.walfi.service.game.MarketService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("/item")
    @ApiOperation(value = "현재 거래소에서 거래 중인 아이템들을 조회")
    public ResponseEntity<HttpResult> getItemList() {

        List<GoodsItemResDto> goodsList = marketService.getItemList();

        HttpResult res = HttpResult.getSuccess();
        res.setData(goodsList);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/character")
    @ApiOperation(value = "현재 거래소에서 거래 중인 캐릭터들을 조회")
    public ResponseEntity<HttpResult> getCharacterList() {

        List<GoodsCharacterResDto> goodsList = marketService.getCharacterList();

        HttpResult res = HttpResult.getSuccess();
        res.setData(goodsList);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/sell")
    @ApiOperation(value = "사용자가 판매하고 싶은 상품을 등록")
    public ResponseEntity<HttpResult> getItemAndCharactor(@ApiIgnore @AuthenticationPrincipal User user, @RequestBody MarketReqDto marketReqDto) {

        marketService.sell(marketReqDto);

        HttpResult res = HttpResult.getSuccess();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/buy")
    @ApiOperation(value = "사용자가 구매하고 싶은 상품을 구매")
    public ResponseEntity<HttpResult> getItemAndCharactor(@ApiIgnore @AuthenticationPrincipal User user, BuyReqDto buyReqDto) {

        marketService.buy(user, buyReqDto);
        HttpResult res = HttpResult.getSuccess();
        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
