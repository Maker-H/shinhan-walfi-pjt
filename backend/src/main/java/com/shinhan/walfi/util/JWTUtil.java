package com.shinhan.walfi.util;

import com.shinhan.walfi.exception.CustomJWTException;
import com.shinhan.walfi.exception.JWTErrorCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.security.Keys;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtil {
//    private final int FIVE_MINUTES = 1000 * 60 * 5; Todo : 구현 하는 동안 5000분으로, 실제로는 5분
    private final int FIVE_MINUTES = 1000 * 60 * 5 * 1000;
    private final int THREE_DAYS = 1000 * 60 * 60 * 24 * 3;

    private final RedisTemplate<String, String> redisTemplate;


    // 헤더 설정
    private final HashMap<String, Object> headerMap = new HashMap<>() {{
        put("alg", "HS256");
        put("typ", "JWT");
    }};

    @Value("${spring.auth.secretKey}")
    private String random256BitKey;

    private SecretKey secretKey;

    @PostConstruct
    private void generateSecretKey() {
        secretKey = Keys.hmacShaKeyFor(random256BitKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createRefreshToken(String userId, String name) {
        Date exp = new Date(getCurrentTime() + THREE_DAYS);

        String refreshToken = Jwts.builder()
                .setHeaderParams(headerMap)
                .setExpiration(exp)
                .setIssuedAt(exp)
                .claim("userId", userId)
                .claim("name", name)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return refreshToken;
    }

    public String createAccessToken(String userId, String name) {
        Date exp = new Date(getCurrentTime() + FIVE_MINUTES);

        String accessToken = Jwts.builder()
                .setHeaderParams(headerMap)
                .setExpiration(exp)
                .setIssuedAt(exp)
                .claim("userId", userId)
                .claim("name", name)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return accessToken;
    }

    public boolean validateToken(String token, HttpServletRequest request) {
        Jws<Claims> jws;

        // 유효한 토큰인지 확인
        try {
            jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            // 유효 기간이 지난 토큰
            CustomJWTException customException = new CustomJWTException(JWTErrorCode.EXPIRED_TOKEN);
            request.setAttribute("exception", customException);
            return false;
        } catch (CompressionException | MalformedJwtException | UnsupportedJwtException e) {
            // 압축 오류, 키 틀림 오류, 해당 토큰과 맞지 않는 토큰 타입 오류
            throw new CustomJWTException(JWTErrorCode.NOT_VALID_TOKEN);
        }
        return true;
    }

    public String getUserIdFromToken(String token) {
        String userId = (String) Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody().get("userId");

        return userId;
    }

    private long getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        return date.getTime();
    }


    /**
     * 유저 Refresh Token 저장
     *
     * @param userEmail 유저 이메일
     * @param refreshToken 현재 유저 Refresh Token
     *
     * @return 0이면 실패, 1이면 성공
     */
    public int saveUserRefreshToken(String userEmail, String refreshToken) {

        if (isNull(userEmail) || isNull(refreshToken)) {
            return 0;
        }

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        // TODO: Refresh Token 유효 시간 정하고, application.yml 파일에서 불러오기
        valueOperations.set(userEmail, refreshToken, 30L, TimeUnit.MINUTES);

        log.info("set '{}' Refresh Token: '{}'", userEmail, refreshToken);

        return 1;
    }

    /**
     * 파라미터로 전달된 이메일의 유저 Refresh Token 반환
     *
     * @param userEmail 유저 이메일
     * @return userRefreshToken or 없거나 만료될 경우 -1
     */
    public String getUserRefreshToken(String userEmail) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String userRefreshToken = valueOperations.get(userEmail);

        log.info("get '{}' Refresh Token: '{}'", userEmail, userRefreshToken);

        if (isNull(userRefreshToken)) {
            userRefreshToken = "-1";
        }

        return userRefreshToken;
    }

    /**
     * 파라미터로 전달된 이메일의 유저 Refresh Token 삭제
     *
     * @param userEmail 유저 이메일
     * @return 0이면 실패, 1이면 성공
     */
    public int deleteUserRefreshToken(String userEmail) {

        if (isNull(userEmail)) {
            return 0;
        }

        Boolean isDeleted = redisTemplate.delete(userEmail);

        if (isDeleted) {
            log.info("delete '{}'", userEmail);
            return 1;
        }
        return 0;
    }
}
