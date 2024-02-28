package com.example.money_lover_backend.controllers;

import com.example.money_lover_backend.models.User;
import com.example.money_lover_backend.models.Wallet;
import com.example.money_lover_backend.services.impl.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/saveWallet")
    public ResponseEntity<Wallet> saveWallet(@RequestBody Wallet wallet) {
        return new ResponseEntity<>(walletService.saveWallet(wallet), HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Iterable<Wallet>> getAllWallet() {
        List<Wallet> wallets = (List<Wallet>) walletService.getAllWallet();
        if (wallets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(wallets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable Long id) {
        return new ResponseEntity<>(walletService.getWalletById(id), HttpStatus.OK);
    }

    @DeleteMapping("/deleteWallet/{id}")
    public ResponseEntity<?> deleteWallet(@PathVariable Long id) {
        walletService.deleteWallet(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editWallet(@RequestBody Wallet wallet, @PathVariable Long id) {
        return new ResponseEntity<>(walletService.editWallet(wallet, id), HttpStatus.OK);
    }

    @GetMapping("/searchWallet")
    public ResponseEntity<?> searchWalletByName(@RequestParam String nameWallet) {
        List<Wallet> wallets = walletService.searchWalletByName(nameWallet);
        return new ResponseEntity<>(wallets, HttpStatus.OK);
    }

    @PutMapping("/{id}/add-money")
    public ResponseEntity<?> addMoneyToWallet(@RequestBody Map<String, Long> moneyMap, @PathVariable Long id) {
        Long moneyToAdd = moneyMap.get("money");
        try {
            Optional<Wallet> wallet = walletService.addMoneyToWallet(id, moneyToAdd);
            return new ResponseEntity<>(wallet, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add money to wallet", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
