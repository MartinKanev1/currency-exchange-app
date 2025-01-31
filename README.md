# Currency Exchange Application
A FullStack application for currency conversion, transaction history, and integration with external exchange rate APIs.

Tech used:
Back End made with Spring Boot.
Database - PostgreSQL.
Front End made with Html, CSS and JavaScript.

## Features
- Exchange Rate Endpoint: Retrieve current exchange rates for any two currencies.
- Currency Conversion Endpoint: Convert amounts between currencies with transaction logging.
- Conversion History Endpoint: Filter and search transaction history by date, source currency, or target currency.
  The search functionality is implemented using JPA Specification with a Specification class and a TransactionSearchDTO, allowing flexible filtering by start date, end date, source currency, target currency, or any combination - 
  of these criteria, as well as individual filters.
- Caching: Optimized API performance with caching for exchange rate data.
- Error Handling: Custom exceptions with meaningful error codes and messages.
- Docker Support: Fully containerized application for consistent environments.
- API Documentation.
- Unit Tests.
