package com.project.currency.exchange.app.Specification;

import com.project.currency.exchange.app.DTOs.TransactionSearchDTO;
import com.project.currency.exchange.app.Model.Transactions;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transactions> filterTransactions(TransactionSearchDTO searchDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (searchDTO.startDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("date"), searchDTO.startDate().atStartOfDay()));
            }


            if (searchDTO.endDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("date"), searchDTO.endDate().atTime(23, 59, 59)));
            }


            if (searchDTO.sourceCurrency() != null && !searchDTO.sourceCurrency().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("sourceCurrency"), searchDTO.sourceCurrency()));
            }


            if (searchDTO.targetCurrency() != null && !searchDTO.targetCurrency().isEmpty()) {
                predicates.add(criteriaBuilder.equal(
                        root.get("targetCurrency"), searchDTO.targetCurrency()));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
