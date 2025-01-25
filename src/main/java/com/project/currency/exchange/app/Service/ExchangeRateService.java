package com.project.currency.exchange.app.Service;

import com.project.currency.exchange.app.Exceptions.ExternalApiException;
import com.project.currency.exchange.app.Exceptions.InvalidCurrencyException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

@Service
public class ExchangeRateService {

    private static final String API_URL = "https://data.fixer.io/api/latest";
    private static final String SYMBOLS_URL = "https://data.fixer.io/api/symbols";
    private final String ACCESS_KEY = "d12cf34ba331afeaf293927cfd4c8540"; // API ключ
    private final RestTemplate restTemplate;

    public ExchangeRateService() {
        this.restTemplate = new RestTemplate();
    }

    @Cacheable(value = "exchangeRates", key = "#sourceCurrency + '_' + #targetCurrency")
    public double getExchangeRate(String sourceCurrency, String targetCurrency) {

        validateCurrency(sourceCurrency);
        validateCurrency(targetCurrency);

        String url = String.format("%s?access_key=%s&symbols=%s,%s", API_URL, ACCESS_KEY, sourceCurrency, targetCurrency);


        Map<String, Object> response = restTemplate.getForObject(url, Map.class);


        if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
            throw new ExternalApiException("Failed to fetch exchange rates. Please check the API response.");
        }


        Map<String, Number> rates = (Map<String, Number>) response.get("rates");
        if (rates == null || !rates.containsKey(sourceCurrency) || !rates.containsKey(targetCurrency)) {

            throw new InvalidCurrencyException("Currency rates not available for the given currencies: "
                    + sourceCurrency + " or " + targetCurrency);
        }


        double sourceRate = rates.get(sourceCurrency).doubleValue();
        double targetRate = rates.get(targetCurrency).doubleValue();


        double rate = targetRate / sourceRate;


        return BigDecimal.valueOf(rate).setScale(4, RoundingMode.HALF_UP).doubleValue();

    }

    @Cacheable("supportedCurrencies")
    public Set<String> getSupportedCurrencies() {
        String url = SYMBOLS_URL + "?access_key=" + ACCESS_KEY;


        Map<String, Object> response = restTemplate.getForObject(url, Map.class);


        if (response == null || !Boolean.TRUE.equals(response.get("success"))) {
            throw new ExternalApiException("Failed to fetch supported currencies from Fixer.io.");
        }


        Map<String, String> symbols = (Map<String, String>) response.get("symbols");
        if (symbols == null || symbols.isEmpty()) {
            throw new RuntimeException("No supported currencies found in the API response.");
        }

        return symbols.keySet();
    }

    public void validateCurrency(String currency) {
        Set<String> validCurrencies = getSupportedCurrencies();
        if (currency == null || !validCurrencies.contains(currency.toUpperCase())) {
            throw new InvalidCurrencyException("Currency " + currency + " is not valid.");
        }
    }


}

