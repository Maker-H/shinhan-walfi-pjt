package com.shinhan.walfi.dto.banking;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AccountResDto {

    private String userId;

    private int accountsLength;

    private List<AccountDto> AccountDtoList;

    /**
     * AccountDto를 AccountResDto로 변환하는 기능
     *
     * @param userId
     * @param accountDtoList
     * @return AccountResDto
     */
    public static AccountResDto getAccountResDto(String userId, List<AccountDto> accountDtoList) {
        return AccountResDto.builder()
                .userId(userId)
                .accountsLength(accountDtoList.size())
                .AccountDtoList(accountDtoList)
                .build();
    }
}
