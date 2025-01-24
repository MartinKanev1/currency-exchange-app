package com.project.currency.exchange.app.Controller;

import com.project.currency.exchange.app.DTOs.TransactionRequest;
import com.project.currency.exchange.app.DTOs.TransactionResponse;
import com.project.currency.exchange.app.DTOs.TransactionSearchDTO;
import com.project.currency.exchange.app.Model.Transactions;
import com.project.currency.exchange.app.Service.ExchangeRateService;
import com.project.currency.exchange.app.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;
    private final ExchangeRateService exchangeRateService;

    public TransactionController(TransactionService transactionService,ExchangeRateService exchangeRateService) {
        this.transactionService = transactionService;
        this.exchangeRateService = exchangeRateService;
    }




    @PostMapping("/convert")
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        // Викаме TransactionService за създаване на транзакцията
        TransactionResponse response = transactionService.createTransaction(request);

        // Връщаме ResponseEntity с резултата
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exchange-rate")
    public ResponseEntity<Double> getExchangeRate(
            @RequestParam String sourceCurrency,
            @RequestParam String targetCurrency) {
        // Извикване на ExchangeRateService за извличане на курса
        double exchangeRate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);

        // Връщане на курса в отговора
        return ResponseEntity.ok(exchangeRate);
    }

    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId) {
        // Изтриваме транзакцията чрез TransactionService
        transactionService.deleteTransaction(transactionId);

        // Връщаме потвърждение
        return ResponseEntity.ok("Transaction with ID " + transactionId + " has been deleted successfully.");
    }



    @PostMapping("/transactions/search")
    public ResponseEntity<List<Transactions>> searchTransactions(@RequestBody TransactionSearchDTO searchDTO) {
        // Извикване на сервиза с критериите за търсене
        List<Transactions> transactions = transactionService.searchTransactions(searchDTO);

        // Връщане на резултатите
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/all")
    public ResponseEntity<List<Transactions>> getAllTransactions() {
        List<Transactions> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Transactions>> getTransactionById(@PathVariable Long id) {
        Optional<Transactions> transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }



}
