package com.ayush.expensetracker.mapper;

import com.ayush.expensetracker.dto.budget.BudgetResponse;
import com.ayush.expensetracker.entity.Budget;

public class BudgetMapper {

    private BudgetMapper() {}

    public static BudgetResponse toResponse(Budget budget){

        return BudgetResponse.builder()
                .id(budget.getId())
                .monthlyLimit(budget.getMonthlyLimit())
                .month(budget.getMonth())
                .year(budget.getYear())
                .build();
    }

}
