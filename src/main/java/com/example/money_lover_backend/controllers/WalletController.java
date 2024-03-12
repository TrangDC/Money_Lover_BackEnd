package com.example.money_lover_backend.controllers;

import com.example.money_lover_backend.models.Budget;
import com.example.money_lover_backend.models.Transaction;
import com.example.money_lover_backend.models.User;
import com.example.money_lover_backend.models.Wallet;
import com.example.money_lover_backend.repositories.UserRepository;
import com.example.money_lover_backend.services.impl.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserRepository userRepository;


    // API gọi danh sách ví của 1 user
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Iterable<Wallet>> getAllWalletByUser(@PathVariable String user_id) {
        List<Wallet> wallets = (List<Wallet>) walletService.getAllWalletByUserId(user_id);
        List<Wallet> activeWallets = new ArrayList<Wallet>();
        for (Wallet wallet: wallets) {
            if (wallet.isActive()) {
                activeWallets.add(wallet);
            }
        }
        if (wallets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(activeWallets, HttpStatus.OK);
    }

    //API gọi 1 ví của 1 user
    @GetMapping("/user/{user_id}/details/{wallet_id}")
    public ResponseEntity<?> getOneWalletByUser(@PathVariable String user_id,
                                                @PathVariable Long wallet_id) {
        List<Wallet> wallets = (List<Wallet>) walletService.getAllWalletByUserId(user_id);
        if (wallets.isEmpty()) {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
        Optional<Wallet> walletOptional = walletService.getWalletById(wallet_id);
        if (walletOptional.isPresent() && wallets.contains(walletOptional.get())) {
            return new ResponseEntity<Wallet>(walletOptional.get(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Wallet not found", HttpStatus.NOT_FOUND);
    }

    //API tạo mới 1 ví cho 1 user
    @PostMapping("/user/{user_id}/create")
    public ResponseEntity<?> create(@PathVariable String user_id, @RequestBody Wallet wallet) {
        List<Wallet> wallets = (List<Wallet>) walletService.getAllWalletByUserId(user_id);
        wallet.setActive(true);
        if (wallets.isEmpty()) {
            return new ResponseEntity<String> ("User not found", HttpStatus.NOT_FOUND);
        }
        if (wallet.getBalance() == null) {
            wallet.setBalance(0L);
        }
        wallets.add(wallet);
        return new ResponseEntity<Wallet>(walletService.saveWallet(wallet), HttpStatus.CREATED);
    }

    //API edit thông tin 1 ví của 1 user
    @PutMapping("/user/{user_id}/edit/{wallet_id}")
    public ResponseEntity<?> edit(@PathVariable String user_id,
                                  @PathVariable String wallet_id,
                                  @RequestBody Wallet wallet) {
        List<Wallet> wallets = (List<Wallet>) walletService.getAllWalletByUserId(user_id);
        if (wallets.isEmpty()) {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
        Optional<Wallet> walletOptional = walletService.getWalletById(Long.valueOf(wallet_id));
        if (walletOptional.isPresent()) {
            wallet.setId(walletOptional.get().getId());
            wallet.setActive(true);
            return new ResponseEntity<Wallet>(walletService.saveWallet(wallet), HttpStatus.OK);
        }
        return new ResponseEntity<>("Wallet not found", HttpStatus.NOT_FOUND);
    }

    //APi thêm tiền vào ví cho 1 user
    @PutMapping("/user/{user_id}/add_money/{wallet_id}/{amount}")
    public ResponseEntity<?> addMoney(@PathVariable String user_id,
                                      @PathVariable String wallet_id,
                                      @PathVariable Long amount) {
        List<Wallet> wallets = (List<Wallet>) walletService.getAllWalletByUserId(user_id);
        if (wallets.isEmpty()) {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
        Optional<Wallet> walletOptional = walletService.getWalletById(Long.valueOf(wallet_id));
        if (walletOptional.isPresent()) {
            Long balance = walletOptional.get().getBalance();
            walletOptional.get().setBalance(amount + balance);
            return new ResponseEntity<Wallet>(walletService.saveWallet(walletOptional.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Wallet not found", HttpStatus.NOT_FOUND);
    }

    //API xóa ví của 1 user
    @DeleteMapping("/user/{user_id}/delete/{wallet_id}")
    public ResponseEntity<?> delete(@PathVariable String user_id,
                                    @PathVariable Long wallet_id) {
        Optional<User> userOptional = userRepository.findById(Long.valueOf(user_id));
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Wallet> wallets = user.getWallets();
            Optional<Wallet> walletOptional = wallets.stream().filter(w -> w.getId().equals(wallet_id)).findFirst();
            if (walletOptional.isPresent()) {
                Wallet wallet = walletOptional.get();

                // Handle transactions linked to the deleted wallet
                List<Transaction> transactionsToRemove = user.getTransactions().stream()
                        .filter(t -> t.getWallet().getId().equals(wallet_id))
                        .collect(Collectors.toList());
                user.getTransactions().removeAll(transactionsToRemove);

                // Handle budgets linked to the deleted wallet
                List<Budget> budgetsToRemove = user.getBudgets().stream()
                        .filter(b -> b.getWallet().getId().equals(wallet_id))
                        .collect(Collectors.toList());
                user.getBudgets().removeAll(budgetsToRemove);

                // Remove the wallet from the user's list of wallets
                user.getWallets().remove(wallet);

                // Save the user to persist the changes
                userRepository.save(user);

                // Delete the wallet
                walletService.delete(wallet_id);

                return new ResponseEntity<>("Wallet deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Wallet not found", HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/{user_id}/deactivate/{wallet_id}")
    public ResponseEntity<?> deactivateWallet(@PathVariable String user_id,
                                              @PathVariable Long wallet_id) {
        List<Wallet> wallets = (List<Wallet>) walletService.getAllWalletByUserId(user_id);
        Optional<User> userOptional = userRepository.findById(Long.valueOf(user_id));
        if (wallets.isEmpty() || userOptional.isEmpty()) {
            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
        }
        User user = userOptional.get();
        Optional<Wallet> walletOptional = walletService.getWalletById(wallet_id);
        if (walletOptional.isPresent() && wallets.contains(walletOptional.get())) {

            // Handle transactions linked to the deleted wallet
            List<Transaction> transactionsToRemove = user.getTransactions().stream()
                    .filter(t -> t.getWallet().getId().equals(wallet_id))
                    .collect(Collectors.toList());
            user.getTransactions().removeAll(transactionsToRemove);

            // Handle budgets linked to the deleted wallet
            List<Budget> budgetsToRemove = user.getBudgets().stream()
                    .filter(b -> b.getWallet().getId().equals(wallet_id))
                    .collect(Collectors.toList());
            user.getBudgets().removeAll(budgetsToRemove);

            Wallet wallet = walletOptional.get();
            wallet.setActive(false);
            walletService.saveWallet(wallet);
            return new ResponseEntity<Wallet>(wallet, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Wallet not found", HttpStatus.NOT_FOUND);
    }

}
