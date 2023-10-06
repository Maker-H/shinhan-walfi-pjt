package com.shinhan.walfi.service.banking;

import com.shinhan.walfi.dto.transfer.TransferDTO;

public interface BankService {

    void localCurrencyTransfer(TransferDTO transferDTO);

    void globalCurrencyTransfer(TransferDTO transferDTO);
}
