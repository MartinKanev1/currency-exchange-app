package com.project.currency.exchange.app.Controller;

import com.project.currency.exchange.app.DTOs.TransactionRequest;
import com.project.currency.exchange.app.DTOs.TransactionResponse;
import com.project.currency.exchange.app.DTOs.TransactionSearchDTO;
import com.project.currency.exchange.app.Model.Transactions;
import com.project.currency.exchange.app.Service.ExchangeRateService;
import com.project.currency.exchange.app.Service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void testCreateTransaction_Success() {

        TransactionRequest request = new TransactionRequest(
                new BigDecimal("100"), "USD", "EUR"
        );

        TransactionResponse response = new TransactionResponse(new BigDecimal("85"), 1L);

        when(transactionService.createTransaction(request)).thenReturn(response);


        ResponseEntity<TransactionResponse> result = transactionController.createTransaction(request);


        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetExchangeRate_Success() {

        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        double exchangeRate = 0.85;

        when(exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency)).thenReturn(exchangeRate);


        ResponseEntity<Double> result = transactionController.getExchangeRate(sourceCurrency, targetCurrency);


        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(exchangeRate, result.getBody());
    }

    @Test
    void testDeleteTransaction_Success() {

        Long transactionId = 1L;

        doNothing().when(transactionService).deleteTransaction(transactionId);


        ResponseEntity<String> result = transactionController.deleteTransaction(transactionId);


        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Transaction with ID 1 has been deleted successfully.", result.getBody());
    }

    @Test
    void testSearchTransactions_Success() {

        TransactionSearchDTO searchDTO = new TransactionSearchDTO(
                LocalDateTime.now().minusDays(1).toLocalDate(),
                LocalDateTime.now().toLocalDate(),
                "USD",
                "EUR"
        );

        Transactions transaction = Transactions.builder()
                .transactionId(1L)
                .amount(new BigDecimal("100"))
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .convertedAmount(new BigDecimal("85"))
                .date(LocalDateTime.now())
                .build();

        when(transactionService.searchTransactions(searchDTO)).thenReturn(List.of(transaction));


        ResponseEntity<List<Transactions>> result = transactionController.searchTransactions(searchDTO);


        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(transaction, result.getBody().get(0));
    }

    @Test
    void testGetAllTransactions_Success() {

        Transactions transaction = Transactions.builder()
                .transactionId(1L)
                .amount(new BigDecimal("100"))
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .convertedAmount(new BigDecimal("85"))
                .date(LocalDateTime.now())
                .build();

        when(transactionService.getAllTransactions()).thenReturn(List.of(transaction));


        ResponseEntity<List<Transactions>> result = transactionController.getAllTransactions();


        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        assertEquals(transaction, result.getBody().get(0));
    }

    @Test
    void testGetTransactionById_Success() {

        Long transactionId = 1L;
        Transactions transaction = Transactions.builder()
                .transactionId(transactionId)
                .amount(new BigDecimal("100"))
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .convertedAmount(new BigDecimal("85"))
                .date(LocalDateTime.now())
                .build();

        when(transactionService.getTransactionById(transactionId)).thenReturn(Optional.of(transaction));


        ResponseEntity<Optional<Transactions>> result = transactionController.getTransactionById(transactionId);


        assertNotNull(result);
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().isPresent());
        assertEquals(transaction, result.getBody().get());
    }
}

