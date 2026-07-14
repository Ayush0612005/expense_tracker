package com.ayush.expensetracker.service;

import com.ayush.expensetracker.dto.budget.BudgetRequest;
import com.ayush.expensetracker.dto.budget.BudgetResponse;
import com.ayush.expensetracker.entity.Budget;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.exception.DuplicateResourceException;
import com.ayush.expensetracker.exception.ResourceNotFoundException;
import com.ayush.expensetracker.mapper.BudgetMapper;
import com.ayush.expensetracker.repository.BudgetRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
    @CacheEvict(
            value = "dashboard",
            allEntries = true
    )
    public BudgetResponse createBudget(BudgetRequest request) {

        User user = getCurrentUser();

        if (budgetRepository.findByUserAndMonthAndYear(
                user,
                request.getMonth(),
                request.getYear()).isPresent()) {

            throw new DuplicateResourceException(
                    "Budget already exists for this month.");
        }

        Budget budget = Budget.builder()
                .monthlyLimit(request.getMonthlyLimit())
                .month(request.getMonth())
                .year(request.getYear())
                .user(user)
                .build();

        budgetRepository.save(budget);

        return BudgetMapper.toResponse(budget);
    }

    public BudgetResponse getCurrentBudget(Integer year,
                                           java.time.Month month) {

        User user = getCurrentUser();

        Budget budget = budgetRepository
                .findByUserAndMonthAndYear(user, month, year)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));

        return BudgetMapper.toResponse(budget);
    }
    @CacheEvict(
            value = "dashboard",
            allEntries = true
    )
    public BudgetResponse updateBudget(Long id,
                                       BudgetRequest request) {

        User user = getCurrentUser();

        Budget budget = budgetRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));

        budget.setMonthlyLimit(request.getMonthlyLimit());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        budgetRepository.save(budget);

        return BudgetMapper.toResponse(budget);
    }

    public void deleteBudget(Long id) {

        User user = getCurrentUser();

        Budget budget = budgetRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Budget not found"));

        budgetRepository.delete(budget);
    }
}
