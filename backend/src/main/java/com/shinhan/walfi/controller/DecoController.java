package com.shinhan.walfi.controller;

import com.shinhan.walfi.dao.ItemDao;
import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.game.GameItem;
import com.shinhan.walfi.dto.game.ItemReqDto;
import com.shinhan.walfi.dto.game.ItemResDto;
import com.shinhan.walfi.service.game.DecoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/deco")
@RequiredArgsConstructor
public class DecoController {

    private final DecoService decoService;

    @GetMapping
    @ApiOperation(value = "사용자가 보유한 동물들과 치장정보를 가져옴")
    public ResponseEntity<HttpResult> getItemAndCharactor(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId = user.getUserId();

        List<ItemResDto> itemResDto = decoService.getList(userId);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(itemResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }


    @PostMapping("/update")
    @ApiOperation(value = "사용자가 세팅한 동물의 치장정보를 저장")
    public ResponseEntity<HttpResult> updateGameItem(@RequestBody ItemReqDto itemReqDto) {

        decoService.update(itemReqDto);

        HttpResult res;
        res = HttpResult.getSuccess();

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/item")
    @ApiOperation(value = "아이템을 랜덤하게 뽑고 결과 반환")
    public ResponseEntity<HttpResult> createRandomItem(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId = user.getUserId();

        ItemDao item = decoService.create(userId);
        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(item);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/itemList")
    @ApiOperation(value = "사용자가 보유하고 있는 아이템 조회")
    public ResponseEntity<HttpResult> getItemList(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId = user.getUserId();

        List<GameItem> itemList = decoService.getItemList(userId);
        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(itemList);

        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
