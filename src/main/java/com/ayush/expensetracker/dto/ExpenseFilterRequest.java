package com.ayush.expensetracker.dto.expense;

import com.ayush.expensetracker.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseFilterRequest {

    private Long categoryId;

    private PaymentMethod paymentMethod;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private int page = 0;

    private int size = 10;

    private String sortBy = "expenseDate";

    private String direction = "desc";
}
