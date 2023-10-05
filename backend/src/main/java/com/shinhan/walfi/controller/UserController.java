package com.shinhan.walfi.controller;

import com.shinhan.walfi.domain.HttpResult;
import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.dto.*;
import com.shinhan.walfi.service.UserService;
import com.shinhan.walfi.util.JWTUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<HttpResult> login(@RequestBody LoginReqDto loginReqDto, HttpServletResponse response){
        TokenDto tokenDto = userService.login(loginReqDto.getUserId(), loginReqDto.getPassword());

        HttpResult res;

        // FIXME: 로그인 예외 처리 Refactoring 하기
        if (tokenDto.isLoginSuccessful()) {
            UserDto userDto = userService.findUserById(loginReqDto.getUserId());

            final String accessToken = tokenDto.getACCESS_TOKEN();
            final String refreshToken = tokenDto.getREFRESH_TOKEN();

            Cookie cookie = new Cookie("Refresh-Token", refreshToken);
            cookie.setMaxAge(60 * 60 * 24 * 3); // 3일
            cookie.setPath("/");
//            cookie.setSecure(true);
//            cookie.setHttpOnly(true);

            response.addHeader("Access-Token", accessToken);
            response.addCookie(cookie);

            res = HttpResult.getSuccess();
            res.setMessage("정상적으로 로그인이 완료됐습니다.");
            res.setData(userDto);
        } else {
            res = new HttpResult(HttpStatus.FORBIDDEN, HttpResult.Result.ERROR,"아이디 혹은 비밀번호가 잘못 됐습니다.");
        }

        return ResponseEntity.status(res.getStatus()).body(res);
    }


    @ApiOperation(value = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<HttpResult> signup(@RequestBody UserSignUpDto userSignUpDto) {
        log.debug("회원가입 아이디 : "+userSignUpDto.getUserId());
        User user = userSignUpDto.UserDtoToEntity();
        userService.signup(user);
        HttpResult result = HttpResult.getSuccess();
        return ResponseEntity.status(result.getStatus()).body(result);
    }
}
