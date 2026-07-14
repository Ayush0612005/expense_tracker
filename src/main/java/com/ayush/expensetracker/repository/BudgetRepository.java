package com.ayush.expensetracker.repository;

import com.ayush.expensetracker.entity.Budget;
import com.ayush.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByUserAndMonthAndYear(
            User user,
            Month month,
            Integer year
    );

    Optional<Budget> findByIdAndUser(
            Long id,
            User user
    );
}
