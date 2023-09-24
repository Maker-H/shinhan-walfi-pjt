package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.*;
import com.shinhan.walfi.dto.game.UserGameInfoDto;
import com.shinhan.walfi.dto.user.UserLoginResDto;
import com.shinhan.walfi.service.UserService;
import com.shinhan.walfi.util.JWTUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "사용자 목록 조회")
    public ResponseEntity<HttpResult> getUserList(){
        List<UserDto> userList = userService.getUserList();

        HttpResult res;
        res = HttpResult.getSuccess();
        res.setData(userList);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public ResponseEntity<UserLoginResDto> login(@RequestBody LoginReqDto loginReqDto, HttpServletResponse response){
        TokenDto tokenDto = userService.login(loginReqDto.getUserId(), loginReqDto.getPassword());

        UserLoginResDto responseDto = null;

        // FIXME: 로그인 예외 처리 Refactoring 하기
        if (tokenDto.isLoginSuccessful()) {

            final String accessToken = tokenDto.getACCESS_TOKEN();
            final String refreshToken = tokenDto.getREFRESH_TOKEN();

            Cookie cookie = new Cookie("Refresh-Token", refreshToken);
            // TODO: application.yml 파일에서 RefreshToken 유효 기간 불러오기
            cookie.setMaxAge(60 * 60 * 24 * 3); // 3일
            cookie.setPath("/");
//            cookie.setSecure(true);
//            cookie.setHttpOnly(true);

            response.addHeader("Access-Token", accessToken);
            response.addCookie(cookie);

            responseDto = UserLoginResDto.builder()
                    .httpStatusCode(200)
                    .message("정상적으로 로그인이 완료됐습니다.")
                    .nickname(tokenDto.getName())
                    .build();
        } else {
            responseDto = UserLoginResDto.builder()
                    .httpStatusCode(401)
                    .message("아이디 혹은 비밀번호가 잘못 됐습니다.")
                    .build();
        }

        return ResponseEntity.status(responseDto.getHttpStatusCode()).body(responseDto);
    }


    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<HttpResult> signup(@RequestBody UserSignUpDto userSignUpDto) {
        log.debug("회원가입 아이디 : "+userSignUpDto.getUserId());
        User user = userSignUpDto.UserDtoToEntity();
        log.debug("회원가입 아이디 : "+user.getUserId());
        userService.signup(user);
        HttpResult result = HttpResult.getSuccess();
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
