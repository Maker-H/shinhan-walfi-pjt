package com.shinhan.walfi.dto.game;

import com.shinhan.walfi.domain.game.UserGameInfo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class UserGameInfoDto {

    private String userId;

    private String username;

    private int point;

    private String status;

    private String 이더잔액;
    /**
     * userGameInfo 정보를 UserGameInfoDto로 변환하는 기능
     *
     * @param userGameInfo
     * @return UserDto
     */
    public static UserGameInfoDto getUserGameInfoDto(UserGameInfo userGameInfo) {
        return UserGameInfoDto.builder()
                .userId(userGameInfo.getUserId())
                .point(userGameInfo.getPoint())
                .status(userGameInfo.getStatus())
                .build();
    }

    public static UserGameInfoDto getUserGameInfoDto(UserGameInfo userGameInfo, String username, String ethBalance) {
        return UserGameInfoDto.builder()
                .userId(userGameInfo.getUserId())
                .point(userGameInfo.getPoint())
                .status(userGameInfo.getStatus())
                .username(username)
                .이더잔액(ethBalance)
                .build();
    }

}
