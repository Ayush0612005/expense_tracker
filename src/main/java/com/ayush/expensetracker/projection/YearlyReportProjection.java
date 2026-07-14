package com.ayush.expensetracker.projection;

import java.math.BigDecimal;

public interface YearlyReportProjection {

    Integer getYear();

    BigDecimal getTotalAmount();

}
