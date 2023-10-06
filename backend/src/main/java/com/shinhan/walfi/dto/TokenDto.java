package com.shinhan.walfi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenDto {

    private boolean isLoginSuccessful;

    private String ACCESS_TOKEN;

    private String REFRESH_TOKEN;

    private String name;
}
