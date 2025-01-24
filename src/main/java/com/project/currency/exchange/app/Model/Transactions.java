package com.project.currency.exchange.app.Model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "transactions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;


    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "source_currency")
    private String sourceCurrency;

    @Column(name = "target_currency")
    private String targetCurrency;

    @Column(name = "converted_amount")
    private BigDecimal convertedAmount;

    @Column(name = "date")
    private LocalDateTime date;



}
