package com.project.currency.exchange.app.DTOs;

import java.math.BigDecimal;

public record TransactionRequest(
    BigDecimal amount,
    String sourceCurrency,
    String targetCurrency
 ) {
}
