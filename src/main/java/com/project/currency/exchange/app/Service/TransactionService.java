package com.project.currency.exchange.app.Service;


import com.project.currency.exchange.app.DTOs.TransactionRequest;
import com.project.currency.exchange.app.DTOs.TransactionResponse;
import com.project.currency.exchange.app.DTOs.TransactionSearchDTO;
import com.project.currency.exchange.app.Exceptions.InvalidAmountException;
import com.project.currency.exchange.app.Exceptions.InvalidCurrencyException;
import com.project.currency.exchange.app.Exceptions.InvalidDateException;
import com.project.currency.exchange.app.Exceptions.ResourceNotFoundException;
import com.project.currency.exchange.app.Model.Transactions;
import com.project.currency.exchange.app.Repository.TransactionRepository;
import com.project.currency.exchange.app.Specification.TransactionSpecification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ExchangeRateService exchangeRateService;

    public TransactionService(TransactionRepository transactionRepository,ExchangeRateService exchangeRateService) {
        this.transactionRepository = transactionRepository;
        this.exchangeRateService = exchangeRateService;
    }



    public TransactionResponse createTransaction(TransactionRequest request) {

        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
        validateCurrency(request.sourceCurrency());
        validateCurrency(request.targetCurrency());


        double exchangeRate = exchangeRateService.getExchangeRate(request.sourceCurrency(), request.targetCurrency());


        BigDecimal convertedAmount = request.amount().multiply(BigDecimal.valueOf(exchangeRate));


        Transactions transaction = Transactions.builder()
                .amount(request.amount())
                .sourceCurrency(request.sourceCurrency())
                .targetCurrency(request.targetCurrency())
                .convertedAmount(convertedAmount)
                .date(LocalDateTime.now())
                .build();


        transactionRepository.save(transaction);


        return new TransactionResponse(convertedAmount, transaction.getTransactionId());
    }

    public void deleteTransaction(Long transactionId) {

        Transactions transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + transactionId));


        transactionRepository.delete(transaction);
    }


    public List<Transactions> searchTransactions(TransactionSearchDTO searchDTO) {
        if (searchDTO.startDate() != null && searchDTO.endDate() != null && searchDTO.startDate().isAfter(searchDTO.endDate())) {
            throw new InvalidDateException("Start date cannot be after end date.");
        }

        validateCurrency(searchDTO.sourceCurrency());
        validateCurrency(searchDTO.targetCurrency());

        return transactionRepository.findAll(TransactionSpecification.filterTransactions(searchDTO));
    }

    public List<Transactions> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transactions> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);

    }

    private void validateCurrency(String currency) {
        Set<String> validCurrencies = exchangeRateService.getSupportedCurrencies();
        if (currency == null || !validCurrencies.contains(currency.toUpperCase())) {
            throw new InvalidCurrencyException("Currency " + currency + " is not valid.");
        }
    }





}
