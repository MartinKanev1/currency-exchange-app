package com.project.currency.exchange.app.Service;

import com.project.currency.exchange.app.Exceptions.ExternalApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    private static final String API_URL = "https://data.fixer.io/api/latest";
    private static final String SYMBOLS_URL = "https://data.fixer.io/api/symbols";
    private static final String ACCESS_KEY = "d12cf34ba331afeaf293927cfd4c8540";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExchangeRate_Success() {

        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        String url = String.format("%s?access_key=%s&symbols=%s,%s", API_URL, ACCESS_KEY, sourceCurrency, targetCurrency);

        Map<String, Object> response = Map.of(
                "success", true,
                "rates", Map.of(
                        "USD", 1.0,
                        "EUR", 0.9521
                )
        );

        when(restTemplate.getForObject(url, Map.class)).thenReturn(response);


        double rate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);




        assertEquals(0.9521, rate);
    }




}

