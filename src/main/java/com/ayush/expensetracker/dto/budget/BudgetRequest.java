package com.ayush.expensetracker.dto.budget;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Month;

@Getter
@Setter
public class BudgetRequest {

    @NotNull
    @DecimalMin("1.0")
    private BigDecimal monthlyLimit;

    @NotNull
    private Month month;

    @NotNull
    private Integer year;
}
