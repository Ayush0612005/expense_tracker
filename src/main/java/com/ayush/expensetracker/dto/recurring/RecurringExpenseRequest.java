package com.ayush.expensetracker.dto.recurring;

import com.ayush.expensetracker.enums.PaymentMethod;
import com.ayush.expensetracker.enums.RecurringFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RecurringExpenseRequest {

    @NotBlank
    private String title;

    @NotNull
    private BigDecimal amount;

    private String description;

    @NotNull
    private PaymentMethod paymentMethod;

    @NotNull
    private RecurringFrequency frequency;

    @NotNull
    private LocalDate nextExecutionDate;

    @NotNull
    private Long categoryId;
}