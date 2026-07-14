package com.ayush.expensetracker.dto.budget;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Month;

@Getter
@Builder
public class BudgetResponse {

    private Long id;

    private BigDecimal monthlyLimit;

    private Month month;

    private Integer year;
}
