package com.ayush.expensetracker.service;

import com.ayush.expensetracker.dto.dashboard.DashboardResponse;
import com.ayush.expensetracker.dto.expense.ExpenseResponse;
import com.ayush.expensetracker.entity.Budget;
import com.ayush.expensetracker.entity.Expense;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.exception.ResourceNotFoundException;
import com.ayush.expensetracker.mapper.ExpenseMapper;
import com.ayush.expensetracker.repository.BudgetRepository;
import com.ayush.expensetracker.repository.ExpenseRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
@Service
public class DashboardService {

    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public DashboardService(ExpenseRepository expenseRepository,
                            BudgetRepository budgetRepository) {
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
    @Cacheable(
            value = "dashboard",
            key = "#root.authentication.name"
    )
    public DashboardResponse getDashboard() {

        User user = getCurrentUser();

        YearMonth current = YearMonth.now();

        Budget budget = budgetRepository
                .findByUserAndMonthAndYear(
                        user,
                        current.getMonth(),
                        current.getYear())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No budget found for current month"));

        BigDecimal monthlySpent = expenseRepository.getMonthlyExpense(
                user,
                current.getYear(),
                current.getMonthValue());

        BigDecimal remaining = budget.getMonthlyLimit().subtract(monthlySpent);

        double percentage =
                monthlySpent.compareTo(BigDecimal.ZERO) == 0
                        ? 0
                        : monthlySpent
                        .multiply(BigDecimal.valueOf(100))
                        .divide(budget.getMonthlyLimit(),2,RoundingMode.HALF_UP)
                        .doubleValue();

        BigDecimal todaySpent =
                expenseRepository.getTodayExpense(user, LocalDate.now());

        long totalExpenses =
                expenseRepository.countByUser(user);

        BigDecimal highestExpense =
                expenseRepository
                        .findTopByUserOrderByAmountDesc(user)
                        .map(Expense::getAmount)
                        .orElse(BigDecimal.ZERO);

        BigDecimal averageExpense =
                expenseRepository.getAverageExpense(user);

        List<ExpenseResponse> recentExpenses =
                expenseRepository
                        .findTop5ByUserOrderByExpenseDateDesc(user)
                        .stream()
                        .map(ExpenseMapper::toResponse)
                        .toList();

        return DashboardResponse.builder()
                .monthlyBudget(budget.getMonthlyLimit())
                .monthlySpent(monthlySpent)
                .remainingBudget(remaining)
                .budgetUsedPercentage(percentage)
                .todaySpent(todaySpent)
                .totalExpenses(totalExpenses)
                .highestExpense(highestExpense)
                .averageExpense(averageExpense)
                .recentExpenses(recentExpenses)
                .build();
    }
}
