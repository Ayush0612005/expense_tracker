package com.ayush.expensetracker.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class SummaryReportResponse {

    private BigDecimal totalExpense;

    private BigDecimal averageExpense;

    private BigDecimal highestExpense;

    private Long totalTransactions;

}
