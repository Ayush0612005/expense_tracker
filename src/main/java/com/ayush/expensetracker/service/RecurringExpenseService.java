package com.ayush.expensetracker.service;

import com.ayush.expensetracker.dto.recurring.RecurringExpenseRequest;
import com.ayush.expensetracker.dto.recurring.RecurringExpenseResponse;
import com.ayush.expensetracker.entity.Category;
import com.ayush.expensetracker.entity.RecurringExpense;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.exception.ResourceNotFoundException;
import com.ayush.expensetracker.mapper.RecurringExpenseMapper;
import com.ayush.expensetracker.repository.CategoryRepository;
import com.ayush.expensetracker.repository.RecurringExpenseRepository;
import com.ayush.expensetracker.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringExpenseService {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final CategoryRepository categoryRepository;
    private final SecurityUtil securityUtil;

    public RecurringExpenseResponse create(
            RecurringExpenseRequest request){

        User user = securityUtil.getCurrentUser();

        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(),user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        RecurringExpense recurringExpense =
                RecurringExpense.builder()
                        .title(request.getTitle())
                        .amount(request.getAmount())
                        .description(request.getDescription())
                        .paymentMethod(request.getPaymentMethod())
                        .frequency(request.getFrequency())
                        .nextExecutionDate(request.getNextExecutionDate())
                        .active(true)
                        .category(category)
                        .user(user)
                        .build();

        recurringExpenseRepository.save(recurringExpense);

        return RecurringExpenseMapper.toResponse(recurringExpense);
    }

    public List<RecurringExpenseResponse> getAll(){

        User user = securityUtil.getCurrentUser();

        return recurringExpenseRepository.findByUser(user)
                .stream()
                .map(RecurringExpenseMapper::toResponse)
                .toList();
    }

    public void delete(Long id){

        User user = securityUtil.getCurrentUser();

        RecurringExpense recurringExpense =
                recurringExpenseRepository
                        .findByIdAndUser(id,user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Recurring Expense not found"));

        recurringExpenseRepository.delete(recurringExpense);
    }
    public RecurringExpenseResponse getById(Long id) {

        User user = securityUtil.getCurrentUser();

        RecurringExpense recurringExpense = recurringExpenseRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recurring expense not found"));

        return RecurringExpenseMapper.toResponse(recurringExpense);
    }
    public RecurringExpenseResponse update(
            Long id,
            RecurringExpenseRequest request) {

        User user = securityUtil.getCurrentUser();

        RecurringExpense recurringExpense = recurringExpenseRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Recurring expense not found"));

        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        recurringExpense.setTitle(request.getTitle());
        recurringExpense.setAmount(request.getAmount());
        recurringExpense.setDescription(request.getDescription());
        recurringExpense.setPaymentMethod(request.getPaymentMethod());
        recurringExpense.setFrequency(request.getFrequency());
        recurringExpense.setNextExecutionDate(request.getNextExecutionDate());
        recurringExpense.setCategory(category);

        recurringExpenseRepository.save(recurringExpense);

        return RecurringExpenseMapper.toResponse(recurringExpense);
    }

}
