package com.ayush.expensetracker.dto.dashboard;

import com.ayush.expensetracker.dto.expense.ExpenseResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private BigDecimal monthlyBudget;

    private BigDecimal monthlySpent;

    private BigDecimal remainingBudget;

    private Double budgetUsedPercentage;

    private BigDecimal todaySpent;

    private Long totalExpenses;

    private BigDecimal highestExpense;

    private BigDecimal averageExpense;

    private List<ExpenseResponse> recentExpenses;

}
