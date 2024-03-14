package com.example.money_lover_backend.payload.request;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class CreateTransaction {
    private Long amount;

    private String note;

    private LocalDate transactionDate;

    private LocalDate endDate;

    private String lender;

    private String borrower;

    private Long wallet_id;

    private Long category_id;

}
