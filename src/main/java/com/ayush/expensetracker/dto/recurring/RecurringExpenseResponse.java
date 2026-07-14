package com.ayush.expensetracker.dto.recurring;

import com.ayush.expensetracker.enums.RecurringFrequency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class RecurringExpenseResponse {

    private Long id;

    private String title;

    private BigDecimal amount;

    private RecurringFrequency frequency;

    private LocalDate nextExecutionDate;

    private boolean active;

}
