package com.shinhan.walfi.domain.banking;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Currency {

    @Id
    private int currencyCode;

    private String currency;

    @OneToMany(mappedBy = "currency")
    private List<BranchCurrency> branchCurrencies = new ArrayList<>();

}
