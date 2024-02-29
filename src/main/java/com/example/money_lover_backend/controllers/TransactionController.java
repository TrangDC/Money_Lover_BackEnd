package com.example.money_lover_backend.controllers;

import com.example.money_lover_backend.models.Transaction;
import com.example.money_lover_backend.services.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/transactions")
public class TransactionController {
//    @Autowired
//    private TransactionService transactionService;
//
//    @GetMapping("")
//    public ResponseEntity<Iterable<Transaction>> getTransaction() {
//        List<Transaction> transactions = (List<Transaction>) transactionService.findAll();
//        if  (transactions.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        return new ResponseEntity<>(transactions, HttpStatus.OK);
//    }
}
