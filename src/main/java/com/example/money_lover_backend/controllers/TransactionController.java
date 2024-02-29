package com.example.money_lover_backend.controllers;

import com.example.money_lover_backend.models.Transaction;
import com.example.money_lover_backend.services.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private ITransactionService transactionService;

    @GetMapping
    public ResponseEntity<Iterable<Transaction>> findAll() {
        List<Transaction> transactions = (List<Transaction>) transactionService.findAll();
        if (transactions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findTransactionById(@PathVariable Long id) {
        Optional<Transaction> transactionOptional = transactionService.findById(id);
        if (!transactionOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(transactionOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Transaction> saveTransaction(@RequestBody Transaction transaction) {
        return new ResponseEntity<>(transactionService.save(transaction), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        Optional<Transaction> transactionOptional = transactionService.findById(id);
        if (!transactionOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        transaction.setId(transactionOptional.get().getId());
        return new ResponseEntity<>(transactionService.save(transaction), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable Long id) {
        Optional<Transaction> transactionOptional = transactionService.findById(id);
        if (!transactionOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        transactionService.remove(id);
        return new ResponseEntity<>(transactionOptional.get(), HttpStatus.OK);
    }
}
