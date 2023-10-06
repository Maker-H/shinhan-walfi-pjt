package com.shinhan.walfi.repository.banking;

import com.shinhan.walfi.domain.banking.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<ExchangeHistory, Long> {
    @Query(value = "select * from exchange_history order by 고시일자 desc, 통화코드 limit 5, 5;", nativeQuery = true)
    List<ExchangeHistory> findYesterday();

//    @Query(value = "select * from exchange_history where 고시일자 = curdate() order by 통화코드;", nativeQuery = true)
//    List<ExchangeHistory> findToday();

    @Query(value = "select * from exchange_history order by 고시일자 desc, 통화코드 limit 5;", nativeQuery = true)
    List<ExchangeHistory> getLatest();
}
