package com.shinhan.walfi.dto.banking;

import com.shinhan.walfi.domain.banking.ExchangeHistory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ExchangeResDto {

    List<ExchangeHistory> exchangeDtoList;

}
