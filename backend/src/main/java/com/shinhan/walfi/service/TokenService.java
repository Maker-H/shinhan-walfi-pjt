package com.shinhan.walfi.service;

public interface TokenService {

    Boolean getValidation(String token);

    String getAccessTokenByRefreshToken(String refreshToken);
}
