package com.shinhan.walfi.mapper;

import com.shinhan.walfi.dao.ItemDao;
import com.shinhan.walfi.domain.enums.ItemName;
import com.shinhan.walfi.domain.game.GameItem;
import com.shinhan.walfi.dto.game.ItemReqDto;
import com.shinhan.walfi.dto.game.ItemResDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DecoMapper {

    List<ItemResDto> getItemCharacter(String userId);

    void update(ItemReqDto itemReqDto);

    void create(ItemDao itemDao);

    int count(ItemDao itemDao);

    List<GameItem> getItemList(String userId);

    void changeOwner(String userId, Long itemIdx);

    ItemName findNameById(Long itemIdx);
}
