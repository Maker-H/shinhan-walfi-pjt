package com.shinhan.walfi.service.banking;

import java.math.BigDecimal;

public interface ProductService {

     void createLevelUpTimeDeposit(String userId,
                            String mainAccountNum,
                            String 통화코드,
                            String 상품명,
                            BigDecimal 금리,
                            long 입금금액);

}
