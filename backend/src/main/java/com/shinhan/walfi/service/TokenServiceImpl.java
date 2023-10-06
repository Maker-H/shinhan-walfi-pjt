package com.shinhan.walfi.service;

import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.exception.CustomJWTException;
import com.shinhan.walfi.exception.JWTErrorCode;
import com.shinhan.walfi.repository.UserRepository;
import com.shinhan.walfi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;


    @Override
    public Boolean getValidation(String token) {
        return jwtUtil.validateToken(token, null);
    }

    @Override
    public String getAccessTokenByRefreshToken(String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new CustomJWTException(JWTErrorCode.TOKEN_IS_NULL);
        }
        jwtUtil.validateToken(refreshToken, null);
        String userId = jwtUtil.getUserIdFromToken(refreshToken);

        Optional<User> ouser = userRepository.findById(userId);

        // Todo : ouser가 null이면 Exception

        return jwtUtil.createAccessToken(ouser.get().getUserId(), ouser.get().getName());
    }
}
