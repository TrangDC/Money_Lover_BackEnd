package com.example.money_lover_backend.controllers;

import com.example.money_lover_backend.models.Transaction;
import com.example.money_lover_backend.models.User;
import com.example.money_lover_backend.models.Wallet;
import com.example.money_lover_backend.repositories.UserRepository;
import com.example.money_lover_backend.services.ITransactionService;
import com.example.money_lover_backend.services.IUserService;
import com.example.money_lover_backend.services.IWaletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @Autowired
    private IWaletService walletService;

    @Autowired
    private UserRepository userRepository;

    //API thêm mới một khoản chi của 1 ví của 1 user


    //API xem chi tiết một khoản chi của 1 ví của 1 user


    //API xem toàn bộ khoản chi của 1 ví của 1 user
    //API xem toàn bộ khoản chi của 1 ví của 1 user
    @GetMapping("/")
    public ResponseEntity<Iterable<Transaction>> findAll() {
        List<Transaction> transactionsempty = new ArrayList<>();
        return new ResponseEntity<>(transactionService.findAll(), HttpStatus.OK);
    }

    //API edit khoản chi của 1 ví của 1 user

    //API xóa khoản chi của 1 ví của 1 user
}
