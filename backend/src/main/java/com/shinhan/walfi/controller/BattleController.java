package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.dto.game.BattleRankResDto;
import com.shinhan.walfi.dto.game.BattleReqDto;
import com.shinhan.walfi.dto.product.ProductResDto;
import com.shinhan.walfi.service.game.BattleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/battle")
@RequiredArgsConstructor
public class BattleController {

    private final BattleService battleService;

    @PostMapping
    @ApiOperation(value = "사용자의 배틀 결과를 기록 (이긴 유저의 exp+50 point+50 로직 포함)")
    public ResponseEntity<HttpResult> record(BattleReqDto battleReqDto){

        // 기록 및 update
        battleService.write(battleReqDto);

        HttpResult res = HttpResult.getSuccess();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping
    @ApiOperation(value = "임시 지점장의 점유 기간을 기준으로 정렬하여 게시")
    public ResponseEntity<HttpResult> getRank(@RequestParam Long idx){
        // 특정 idx 브랜치의 랭킹 정보를 반환한다.

        List<BattleRankResDto> list = battleService.getRank(idx);

        HttpResult res = HttpResult.getSuccess();
        res.setData(list);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/allrank")
    @ApiOperation(value = "전체 지점을 대상으로 임시 지점장의 점유 기간을 기준으로 정렬하여 게시")
    public ResponseEntity<HttpResult> getAllRank(){
        // 전체 지점 점유 랭킹 정보를 반환한다.

        List<BattleRankResDto> list = battleService.getAllRank();

        HttpResult res = HttpResult.getSuccess();
        res.setData(list);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/topten")
    @ApiOperation(value = "입력한 아이디의 유저가 탑텐 유저가 맞다면 적용되는 금리를 아니면 0을 반환")
    public ResponseEntity<HttpResult> getToptenRate(@RequestParam String userId){
        ProductResDto productResDto = battleService.getRate(userId);

        HttpResult res = HttpResult.getSuccess();
        res.setData(productResDto);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/count")
    @ApiOperation(value = "유저가 졌을때 배틀 카운트")
    public ResponseEntity<HttpResult> getBattleCount(@RequestParam String userId){
        battleService.getBattleCount(userId);

        HttpResult res = HttpResult.getSuccess();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/getbattlecount")
    @ApiOperation(value = "유저가 배틀을 몇 번 수행했는지 확인")
    public ResponseEntity<HttpResult> getUserBattleHistoryCount(@RequestParam String userId){
        ProductResDto productResDto = battleService.getUserBattleHistoryCount(userId);

        HttpResult res = HttpResult.getSuccess();
        res.setData(productResDto);
        return ResponseEntity.status(res.getStatus()).body(res);
    }


}
