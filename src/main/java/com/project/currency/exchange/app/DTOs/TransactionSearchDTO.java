package com.project.currency.exchange.app.DTOs;

import java.time.LocalDate;

public record TransactionSearchDTO(
        LocalDate startDate,
        LocalDate endDate,
        String sourceCurrency,
        String targetCurrency
) {

}
