package com.project.currency.exchange.app.DTOs;

import java.math.BigDecimal;

public record TransactionResponse(

        BigDecimal convertedAmount,
        Long transactionId

) {
}
