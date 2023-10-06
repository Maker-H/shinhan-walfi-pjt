package com.shinhan.walfi.dao;

import com.shinhan.walfi.domain.enums.ItemName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDao {

    private ItemName itemName;

    private String userId;

}
