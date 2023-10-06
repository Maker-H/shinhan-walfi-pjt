package com.shinhan.walfi.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResDto {

    private int httpStatusCode;

    private String message;

    private String name;
}

