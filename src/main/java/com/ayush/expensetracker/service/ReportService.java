package com.ayush.expensetracker.service;

import com.ayush.expensetracker.dto.report.*;
import com.ayush.expensetracker.entity.Expense;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.projection.CategoryReportProjection;
import com.ayush.expensetracker.projection.MonthlyReportProjection;
import com.ayush.expensetracker.projection.YearlyReportProjection;
import com.ayush.expensetracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ExpenseRepository expenseRepository;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
    @Cacheable(
            value = "categoryReport",
            key = "#startDate + '-' + #endDate + '-' + #root.authentication.name"
    )
    public List<CategoryReportResponse> getCategoryReport(
            int year,
            int month) {

        User user = getCurrentUser();

        return expenseRepository
                .getCategoryReport(user, year, month)
                .stream()
                .map(this::mapCategory)
                .toList();
    }
    @Cacheable(
            value = "monthlyReport",
            key = "#year + '-' + #month + '-' + #root.authentication.name"
    )
    public List<MonthlyReportResponse> getMonthlyReport(
            int year) {

        User user = getCurrentUser();

        return expenseRepository
                .getMonthlyReport(user, year)
                .stream()
                .map(this::mapMonthly)
                .toList();
    }

    public List<YearlyReportResponse> getYearlyReport() {

        User user = getCurrentUser();

        return expenseRepository
                .getYearlyReport(user)
                .stream()
                .map(this::mapYearly)
                .toList();
    }
    @Cacheable(
            value = "summary",
            key = "#root.authentication.name"
    )
    public SummaryReportResponse getSummaryReport() {

        User user = getCurrentUser();

        BigDecimal average =
                expenseRepository.getAverageExpense(user);

        BigDecimal highest =
                expenseRepository
                        .findTopByUserOrderByAmountDesc(user)
                        .map(Expense::getAmount)
                        .orElse(BigDecimal.ZERO);

        long total =
                expenseRepository.countByUser(user);

        BigDecimal monthly =
                expenseRepository.getMonthlyExpense(
                        user,
                        java.time.Year.now().getValue(),
                        java.time.LocalDate.now().getMonthValue());

        return SummaryReportResponse.builder()
                .totalExpense(monthly)
                .averageExpense(average)
                .highestExpense(highest)
                .totalTransactions(total)
                .build();
    }

    private CategoryReportResponse mapCategory(
            CategoryReportProjection p) {

        return CategoryReportResponse.builder()
                .category(p.getCategory())
                .totalAmount(p.getTotalAmount())
                .build();
    }

    private MonthlyReportResponse mapMonthly(
            MonthlyReportProjection p) {

        return MonthlyReportResponse.builder()
                .month(p.getMonth())
                .totalAmount(p.getTotalAmount())
                .build();
    }

    private YearlyReportResponse mapYearly(
            YearlyReportProjection p) {

        return YearlyReportResponse.builder()
                .year(p.getYear())
                .totalAmount(p.getTotalAmount())
                .build();
    }

}