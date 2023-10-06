package com.shinhan.walfi.service.game;

import com.shinhan.walfi.dao.ItemDao;
import com.shinhan.walfi.domain.game.GameItem;
import com.shinhan.walfi.dto.game.ItemReqDto;
import com.shinhan.walfi.dto.game.ItemResDto;

import java.util.List;

public interface DecoService {

    List<ItemResDto> getList(String userId);

    void update(ItemReqDto itemReqDto);

    ItemDao create(String userId);

    List<GameItem> getItemList(String userId);
}
