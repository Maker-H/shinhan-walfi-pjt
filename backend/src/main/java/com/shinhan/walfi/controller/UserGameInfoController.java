package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.UserReqDto;
import com.shinhan.walfi.dto.game.UserGameInfoDto;
import com.shinhan.walfi.dto.game.UserGamePointReqDto;
import com.shinhan.walfi.service.game.UserGameInfoService;
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
@RequestMapping("/game")
@RequiredArgsConstructor
public class UserGameInfoController {

    private final UserGameInfoService userGameInfoService;

    @PostMapping("/getinfo")
    @ApiOperation(value = "아이디에 해당하는 유저의 게임 정보를 가져오는 기능")
    public ResponseEntity<HttpResult> getUserGameInfo(@ApiIgnore @AuthenticationPrincipal User user){
        UserGameInfoDto userGameInfoDto = userGameInfoService.getUserGameInfo(user.getUserId());

        HttpResult res;
        res = HttpResult.getSuccess();
        res.setData(userGameInfoDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/pointup")
    @ApiOperation(value = "아이디에 해당하는 유저의 포인트를 상승시키는 기능")
    public ResponseEntity<HttpResult> updatePoint(@ApiIgnore @AuthenticationPrincipal User user,@RequestBody UserGamePointReqDto userGamePointReqDto){
        UserGameInfoDto userGameInfoDto = userGameInfoService
                .updatePoint(user.getUserId(), userGamePointReqDto.getPoint());

        HttpResult res;
        res = HttpResult.getSuccess();
        res.setData(userGameInfoDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
