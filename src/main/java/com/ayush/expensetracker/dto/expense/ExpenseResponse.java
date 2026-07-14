package com.ayush.expensetracker.dto.expense;

import com.ayush.expensetracker.enums.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ExpenseResponse {

    private Long id;

    private String title;

    private BigDecimal amount;

    private String description;

    private LocalDate expenseDate;

    private PaymentMethod paymentMethod;

    private String category;
}
