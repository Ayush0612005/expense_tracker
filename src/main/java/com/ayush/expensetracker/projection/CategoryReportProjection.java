package com.ayush.expensetracker.projection;


import java.math.BigDecimal;

public interface CategoryReportProjection {

    String getCategory();

    BigDecimal getTotalAmount();

}
