package com.shinhan.walfi.dto.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResDto {

    private String 상품명;

    private String 기본금리;

    private String 추가금리;

    private String 총금리;

    private String 가입기간;

}
