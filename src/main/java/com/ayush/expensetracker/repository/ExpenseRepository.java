package com.ayush.expensetracker.repository;

import com.ayush.expensetracker.entity.Expense;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.projection.CategoryReportProjection;
import com.ayush.expensetracker.projection.MonthlyReportProjection;
import com.ayush.expensetracker.projection.YearlyReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends
        JpaRepository<Expense, Long>,
        JpaSpecificationExecutor<Expense> {

    // Existing methods
    List<Expense> findByUser(User user);

    Optional<Expense> findByIdAndUser(Long id, User user);

    // Dashboard methods
    List<Expense> findTop5ByUserOrderByExpenseDateDesc(User user);

    long countByUser(User user);

    Optional<Expense> findTopByUserOrderByAmountDesc(User user);

    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.user = :user
              AND YEAR(e.expenseDate) = :year
              AND MONTH(e.expenseDate) = :month
            """)
    BigDecimal getMonthlyExpense(
            @Param("user") User user,
            @Param("year") int year,
            @Param("month") int month
    );

    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.user = :user
              AND e.expenseDate = :date
            """)
    BigDecimal getTodayExpense(
            @Param("user") User user,
            @Param("date") LocalDate date
    );

    @Query("""
            SELECT COALESCE(AVG(e.amount), 0)
            FROM Expense e
            WHERE e.user = :user
            """)
    BigDecimal getAverageExpense(
            @Param("user") User user
    );

    @Query("""
SELECT
c.name as category,
SUM(e.amount) as totalAmount
FROM Expense e
JOIN e.category c
WHERE e.user=:user
AND YEAR(e.expenseDate)=:year
AND MONTH(e.expenseDate)=:month
GROUP BY c.name
ORDER BY SUM(e.amount) DESC
""")
    List<CategoryReportProjection> getCategoryReport(
            @Param("user") User user,
            @Param("year") int year,
            @Param("month") int month);

    @Query("""
SELECT
MONTH(e.expenseDate) as month,
SUM(e.amount) as totalAmount
FROM Expense e
WHERE e.user=:user
AND YEAR(e.expenseDate)=:year
GROUP BY MONTH(e.expenseDate)
ORDER BY MONTH(e.expenseDate)
""")
    List<MonthlyReportProjection> getMonthlyReport(
            @Param("user") User user,
            @Param("year") int year);

    @Query("""
SELECT
YEAR(e.expenseDate) as year,
SUM(e.amount) as totalAmount
FROM Expense e
WHERE e.user=:user
GROUP BY YEAR(e.expenseDate)
ORDER BY YEAR(e.expenseDate)
""")
    List<YearlyReportProjection> getYearlyReport(
            @Param("user") User user);
}
