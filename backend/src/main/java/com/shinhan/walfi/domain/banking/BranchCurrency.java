package com.shinhan.walfi.domain.banking;

import com.shinhan.walfi.domain.game.Branch;
import lombok.Getter;

import javax.persistence.*;


import static javax.persistence.FetchType.*;

@Entity
@Getter
public class BranchCurrency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long branchCurrencyIdx;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "branchIdx")
    private Branch branch;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "curencyCode")
    private Currency currency;

}
