package com.shinhan.walfi.service.game;

import com.shinhan.walfi.dto.game.UserGameInfoDto;

public interface UserGameInfoService {

    UserGameInfoDto getUserGameInfo(String userId);

    UserGameInfoDto updatePoint(String userId, int point);

}
