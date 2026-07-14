package com.ayush.expensetracker.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
public class MonthlyReportResponse {

    private Integer month;

    private BigDecimal totalAmount;

}
