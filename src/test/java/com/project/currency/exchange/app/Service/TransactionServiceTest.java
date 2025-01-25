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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_Success() {

        TransactionRequest request = new TransactionRequest(
                new BigDecimal("100"), "USD", "EUR"
        );

        LocalDateTime fixedDate = LocalDateTime.of(2025, 1, 25, 10, 0);

        Transactions savedTransaction = Transactions.builder()
                .amount(new BigDecimal("100"))
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .convertedAmount(new BigDecimal("85"))
                .date(fixedDate)
                .transactionId(1L)
                .build();

        when(exchangeRateService.getSupportedCurrencies()).thenReturn(Set.of("USD", "EUR"));
        when(exchangeRateService.getExchangeRate("USD", "EUR")).thenReturn(0.85);
        when(transactionRepository.save(any(Transactions.class))).thenReturn(savedTransaction);


        TransactionResponse response = transactionService.createTransaction(request);


        assertNotNull(response);
        assertEquals(new BigDecimal("85"), response.convertedAmount());
        assertEquals(1L, response.transactionId());

        ArgumentCaptor<Transactions> captor = ArgumentCaptor.forClass(Transactions.class);
        verify(transactionRepository).save(captor.capture());
        Transactions capturedTransaction = captor.getValue();

        assertEquals(new BigDecimal("100"), capturedTransaction.getAmount());
        assertEquals("USD", capturedTransaction.getSourceCurrency());
        assertEquals("EUR", capturedTransaction.getTargetCurrency());
        assertEquals(new BigDecimal("85"), capturedTransaction.getConvertedAmount());
    }




    @Test
    void testCreateTransaction_InvalidAmount() {

        TransactionRequest request = new TransactionRequest(
                new BigDecimal("-10"), "USD", "EUR"
        );


        assertThrows(InvalidAmountException.class, () -> transactionService.createTransaction(request));
    }

    @Test
    void testCreateTransaction_InvalidCurrency() {

        TransactionRequest request = new TransactionRequest(
                new BigDecimal("100"), "INVALID", "EUR"
        );


        when(exchangeRateService.getSupportedCurrencies()).thenReturn(Set.of("USD", "EUR"));


        assertThrows(InvalidCurrencyException.class, () -> transactionService.createTransaction(request));


        verify(exchangeRateService, times(1)).getSupportedCurrencies();
    }



    @Test
    void testDeleteTransaction_Success() {

        Transactions transaction = Transactions.builder()
                .transactionId(1L)
                .build();

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));


        transactionService.deleteTransaction(1L);


        verify(transactionRepository, times(1)).delete(transaction);
        verify(transactionRepository, times(1)).findById(1L);
    }


    @Test //working
    void testDeleteTransaction_NotFound() {

        lenient().when(transactionRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class, () -> transactionService.deleteTransaction(1L));


        verify(transactionRepository, never()).delete((Transactions) any());
    }

    @Test
    void testSearchTransactions_Success() {

        TransactionSearchDTO searchDTO = new TransactionSearchDTO(
                LocalDate.now().minusDays(1),
                LocalDate.now(),
                "USD",
                "EUR"
        );

        Transactions transaction = Transactions.builder()
                .transactionId(1L)
                .sourceCurrency("USD")
                .targetCurrency("EUR")
                .amount(new BigDecimal("100"))
                .build();


        lenient().when(exchangeRateService.getSupportedCurrencies()).thenReturn(Set.of("USD", "EUR"));


        when(transactionRepository.findAll(any(Specification.class))).thenReturn(List.of(transaction));


        List<Transactions> result = transactionService.searchTransactions(searchDTO);


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("USD", result.get(0).getSourceCurrency());
        assertEquals("EUR", result.get(0).getTargetCurrency());

        verify(transactionRepository, times(1)).findAll(any(Specification.class));
    }







    @Test //working
    void testSearchTransactions_InvalidDate() {

        TransactionSearchDTO searchDTO = new TransactionSearchDTO(
                LocalDate.now().plusDays(1),
                LocalDate.now(),
                "USD",
                "EUR"
        );


        lenient().when(exchangeRateService.getSupportedCurrencies()).thenReturn(Set.of("USD", "EUR"));


        assertThrows(InvalidDateException.class, () -> transactionService.searchTransactions(searchDTO));
    }






}

