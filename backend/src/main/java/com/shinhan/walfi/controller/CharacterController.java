package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;

import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.game.*;
import com.shinhan.walfi.dto.product.ProductResDto;
import com.shinhan.walfi.service.game.CharacterService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/character")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping("/create")
    @ApiOperation(value = "사용자의 계정으로 새로운 캐릭터를 랜덤한 타입으로 생성")
    public ResponseEntity<HttpResult> createRandomCharacter(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId = user.getUserId();
        CharacterWithUserIdResDto characterWithUserIdResDto = characterService.create(userId);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(characterWithUserIdResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/shop")
    @ApiOperation(value = "사용자의 계정으로 캐릭터 1개 뽑기")
    public ResponseEntity<HttpResult> shopRandomCharacter(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId =user.getUserId();
        CharacterWithUserIdResDto characterWithUserIdResDto = characterService.shop(userId);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(characterWithUserIdResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/shopten/")
    @ApiOperation(value = "사용자의 계정으로 캐릭터 10개뽑기")
    public ResponseEntity<HttpResult> shopRandomTenCharacter(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId = user.getUserId();

        CharacterListResDto characterListResDto = characterService.shopTen(userId);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(characterListResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/getcharacters")
    @ApiOperation(value = "사용자가 보유하고 있는 캐릭터 목록을 조회")
    public ResponseEntity<HttpResult> getCharacters(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId = user.getUserId();
        CharacterListResDto characterListResDto = characterService.searchCharacters(userId);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(characterListResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/getmain")
    @ApiOperation(value = "사용자가 보유하고 있는 메인 캐릭터를 조회")
    public ResponseEntity<HttpResult> getMainCharacter(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId = user.getUserId();
        CharacterWithUserIdResDto characterWithUserIdResDto = characterService.searchMainCharacter(userId);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(characterWithUserIdResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/change/color")
    @ApiOperation(value = "색을 랜덤으로 골라 사용자의 메인 캐릭터의 색을 변화시키는 기능",
    notes = "act='밥먹기'와 atk를 같이 보내야 함" +
            "act='훈련하기'와 def를 같이 보내야 함")
    public ResponseEntity<HttpResult> changeCharacterColor(@ApiIgnore @AuthenticationPrincipal User user, @RequestBody MainCharacterReqDto mainCharacterReqDto) {
        String userId = user.getUserId();
        Long mainCharacterIdx = mainCharacterReqDto.getMainCharacterIdx();

        CharacterWithUserIdResDto characterWithUserIdResDto = characterService.changeCharacterColor(userId, mainCharacterIdx);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(characterWithUserIdResDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/change/status")
    @ApiOperation(value = "사용자가 보유하고 있는 캐릭터의 스텟(atk, def, exp, isMain, point)를 상승/변화 시킴")
    public ResponseEntity<HttpResult> changeCharacterStatus(@ApiIgnore @AuthenticationPrincipal User user, @RequestBody CharacterStatusReqDto characterStatusReqDto) {
        String userId = user.getUserId();
        Long characterIdx = characterStatusReqDto.getCharacterIdx();
        String statusType = characterStatusReqDto.getStatusType();
        int statusValue = characterStatusReqDto.getValue();
        String act = characterStatusReqDto.getAct();

        CharacterWithUserIdResDto characterWithUserIdResDto
                = characterService.updateCharacterStatus(userId, characterIdx, statusType, statusValue, act);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(characterWithUserIdResDto);
        res.setMessage(act.concat(" 완료"));

        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/maxcharacter")
    @ApiOperation(value = "사용자가 보유하고 MAX 레벨 캐릭터 수 반환")
    public ResponseEntity<HttpResult> getMaxLevelCharacterNum(@ApiIgnore @AuthenticationPrincipal User user) {
        String userId = user.getUserId();

        ProductResDto maxLevelCharacterNumDto = characterService.getMaxLevelCharacterNum(userId);

        HttpResult res;

        res = HttpResult.getSuccess();
        res.setData(maxLevelCharacterNumDto);

        return ResponseEntity.status(res.getStatus()).body(res);
    }
}
