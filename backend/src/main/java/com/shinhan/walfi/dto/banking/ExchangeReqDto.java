package com.shinhan.walfi.dto.banking;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class ExchangeReqDto {

    private HashMap<String, String> dataHeader;

    private HashMap<String, String> dataBody;

}
