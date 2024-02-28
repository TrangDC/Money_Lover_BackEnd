package com.example.money_lover_backend.services;
import com.example.money_lover_backend.models.Wallet;
import java.util.Optional;

public interface IWaletService{
    public Wallet saveWallet(Wallet wallet);

    public Iterable<Wallet> getAllWallet();

    Iterable<Wallet> searchWalletByName(String walletName);

    Optional<Wallet> getWalletById(Long id);

    public String delete(Long id);


    Optional<Wallet> addMoneyToWallet(Long walletId, Long moneyToAdd);

    public Wallet editWallet(Wallet wallet,Long id);

    Iterable<Wallet> getAllWalletByUserId(String id);
}
