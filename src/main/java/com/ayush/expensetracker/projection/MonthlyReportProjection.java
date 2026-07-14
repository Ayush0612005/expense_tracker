package com.ayush.expensetracker.projection;


import java.math.BigDecimal;

public interface MonthlyReportProjection {

    Integer getMonth();

    BigDecimal getTotalAmount();

}
