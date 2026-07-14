package com.ayush.expensetracker.mapper;

import com.ayush.expensetracker.dto.expense.ExpenseResponse;
import com.ayush.expensetracker.entity.Expense;

public class ExpenseMapper {

    private ExpenseMapper() {
    }

    public static ExpenseResponse toResponse(Expense expense) {

        return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .expenseDate(expense.getExpenseDate())
                .paymentMethod(expense.getPaymentMethod())
                .category(expense.getCategory().getName())
                .build();
    }
}
