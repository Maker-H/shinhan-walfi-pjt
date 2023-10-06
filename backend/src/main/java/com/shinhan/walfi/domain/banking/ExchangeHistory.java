package com.shinhan.walfi.domain.banking;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long 고시번호;

    private Date 고시일자;

    private String 통화코드; // "USD" // cur_unit

    private String 통화명; // "미국 달러" cur_nm

    private float 전신환매입환율; // ttb

    private float 전신환매도환율; // tts

    private float 매매기준환율; // deal_bas_r

    private float 전일대비 = 0;

    @Builder
    public ExchangeHistory(Date 고시일자, String 통화코드, String 통화명, float 전신환매입환율, float 전신환매도환율, float 매매기준환율){
        this.고시일자 = 고시일자;
        this.통화코드 = 통화코드;
        this.통화명 = 통화명;
        this.전신환매입환율 = 전신환매입환율;
        this.전신환매도환율 = 전신환매도환율;
        this.매매기준환율 = 매매기준환율;
    }
}
