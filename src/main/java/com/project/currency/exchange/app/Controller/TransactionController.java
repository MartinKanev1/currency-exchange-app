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
        
        TransactionResponse response = transactionService.createTransaction(request);

        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exchange-rate")
    public ResponseEntity<Double> getExchangeRate(
            @RequestParam String sourceCurrency,
            @RequestParam String targetCurrency) {
        
        double exchangeRate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);

        
        return ResponseEntity.ok(exchangeRate);
    }

    @DeleteMapping("/transactions/{transactionId}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long transactionId) {
        
        transactionService.deleteTransaction(transactionId);

        
        return ResponseEntity.ok("Transaction with ID " + transactionId + " has been deleted successfully.");
    }



    @PostMapping("/transactions/search")
    public ResponseEntity<List<Transactions>> searchTransactions(@RequestBody TransactionSearchDTO searchDTO) {
       
        List<Transactions> transactions = transactionService.searchTransactions(searchDTO);

        
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
