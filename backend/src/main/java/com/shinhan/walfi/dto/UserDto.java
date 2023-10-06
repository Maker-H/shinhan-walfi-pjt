package com.shinhan.walfi.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {

    private String userId;

    private String email;

    private String name;

    private LocalDateTime birthDate;

    private String phoneNumber;

    private String userMainAccount;

}
