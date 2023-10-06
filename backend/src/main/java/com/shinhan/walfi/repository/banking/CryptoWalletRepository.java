package com.shinhan.walfi.repository.banking;


import com.shinhan.walfi.domain.User;
import com.shinhan.walfi.domain.banking.CryptoWallet;
import com.shinhan.walfi.domain.enums.CoinType;
import com.shinhan.walfi.domain.game.UserGameInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CryptoWalletRepository {

    private final EntityManager em;

    public CryptoWallet save(CryptoWallet cryptoWallet) {
        em.persist(cryptoWallet);
        return cryptoWallet;
    }

    public List<CryptoWallet> findCryptoWallets(User user) {
        List<CryptoWallet> wallets = em.createQuery("select c from CryptoWallet c where user=:user", CryptoWallet.class)
                .setParameter("user", user)
                .getResultList();

        return wallets;
    }

    public CryptoWallet findWallet(String accountNum, CoinType coinType) {
        return em.createQuery("select c from CryptoWallet c where c.user.대표계좌=:accountNum and coinType=:coinType", CryptoWallet.class)
                .setParameter("accountNum", accountNum)
                .setParameter("coinType", coinType)
                .getSingleResult();
    }

}
