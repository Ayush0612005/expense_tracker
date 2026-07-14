package com.ayush.expensetracker.mapper;

import com.ayush.expensetracker.dto.recurring.RecurringExpenseResponse;
import com.ayush.expensetracker.entity.RecurringExpense;

public class RecurringExpenseMapper {

    private RecurringExpenseMapper() {
    }

    public static RecurringExpenseResponse toResponse(
            RecurringExpense recurringExpense) {

        return RecurringExpenseResponse.builder()
                .id(recurringExpense.getId())
                .title(recurringExpense.getTitle())
                .amount(recurringExpense.getAmount())
                .frequency(recurringExpense.getFrequency())
                .nextExecutionDate(recurringExpense.getNextExecutionDate())
                .active(recurringExpense.isActive())
                .build();
    }
}
