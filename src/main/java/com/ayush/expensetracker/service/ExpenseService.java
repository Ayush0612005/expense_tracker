package com.ayush.expensetracker.service;

import com.ayush.expensetracker.dto.expense.ExpenseFilterRequest;
import com.ayush.expensetracker.dto.expense.ExpenseRequest;
import com.ayush.expensetracker.dto.expense.ExpenseResponse;
import com.ayush.expensetracker.entity.Category;
import com.ayush.expensetracker.entity.Expense;
import com.ayush.expensetracker.entity.User;
import com.ayush.expensetracker.exception.ResourceNotFoundException;
import com.ayush.expensetracker.mapper.ExpenseMapper;
import com.ayush.expensetracker.repository.CategoryRepository;
import com.ayush.expensetracker.repository.ExpenseRepository;
import com.ayush.expensetracker.specification.ExpenseSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
    @CacheEvict(
            value = {
                    "dashboard",
                    "summary",
                    "monthlyReport",
                    "categoryReport"
            },
            allEntries = true
    )
    public ExpenseResponse createExpense(ExpenseRequest request) {

        User user = getCurrentUser();

        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .description(request.getDescription())
                .expenseDate(request.getExpenseDate())
                .paymentMethod(request.getPaymentMethod())
                .category(category)
                .user(user)
                .build();

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponse(savedExpense);
    }

    public Page<ExpenseResponse> getAllExpenses(ExpenseFilterRequest filter) {

        User user = getCurrentUser();

        Sort sort = filter.getDirection().equalsIgnoreCase("desc")
                ? Sort.by(filter.getSortBy()).descending()
                : Sort.by(filter.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                sort
        );

        Specification<Expense> specification =
                ExpenseSpecification.belongsToUser(user);

        if (filter.getCategoryId() != null) {

            Category category = categoryRepository
                    .findByIdAndUser(filter.getCategoryId(), user)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Category not found"));

            specification = specification.and(
                    ExpenseSpecification.hasCategory(category));
        }

        if (filter.getPaymentMethod() != null) {

            specification = specification.and(
                    ExpenseSpecification.hasPaymentMethod(
                            filter.getPaymentMethod()));
        }

        if (filter.getStartDate() != null &&
                filter.getEndDate() != null) {

            specification = specification.and(
                    ExpenseSpecification.expenseBetween(
                            filter.getStartDate(),
                            filter.getEndDate()));
        }

        if (filter.getMinAmount() != null &&
                filter.getMaxAmount() != null) {

            specification = specification.and(
                    ExpenseSpecification.amountBetween(
                            filter.getMinAmount(),
                            filter.getMaxAmount()));
        }

        Page<Expense> page =
                expenseRepository.findAll(specification, pageable);

        return page.map(ExpenseMapper::toResponse);
    }

    public ExpenseResponse getExpense(Long id) {

        User user = getCurrentUser();

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found"));

        return ExpenseMapper.toResponse(expense);
    }
    @CacheEvict(
            value = {
                    "dashboard",
                    "summary",
                    "monthlyReport",
                    "categoryReport"
            },
            allEntries = true
    )
    public ExpenseResponse updateExpense(Long id,
                                         ExpenseRequest request) {

        User user = getCurrentUser();

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found"));

        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setCategory(category);

        Expense updatedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toResponse(updatedExpense);
    }
    @CacheEvict(
            value = {
                    "dashboard",
                    "summary",
                    "monthlyReport",
                    "categoryReport"
            },
            allEntries = true
    )
    public void deleteExpense(Long id) {

        User user = getCurrentUser();

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found"));

        expenseRepository.delete(expense);
    }
}





//package com.ayush.expensetracker.service;
//
//import com.ayush.expensetracker.dto.expense.ExpenseFilterRequest;
//import com.ayush.expensetracker.dto.expense.ExpenseRequest;
//import com.ayush.expensetracker.dto.expense.ExpenseResponse;
//import com.ayush.expensetracker.entity.Category;
//import com.ayush.expensetracker.entity.Expense;
//import com.ayush.expensetracker.entity.User;
//import com.ayush.expensetracker.event.ExpenseCreatedEvent;
//import com.ayush.expensetracker.exception.ResourceNotFoundException;
//import com.ayush.expensetracker.mapper.ExpenseMapper;
//import com.ayush.expensetracker.producer.ExpenseEventProducer;
//import com.ayush.expensetracker.repository.CategoryRepository;
//import com.ayush.expensetracker.repository.ExpenseRepository;
//import com.ayush.expensetracker.specification.ExpenseSpecification;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ExpenseService {
//
//    private final ExpenseRepository expenseRepository;
//    private final CategoryRepository categoryRepository;
////    private final ExpenseEventProducer producer;
//
//    private User getCurrentUser() {
//        return (User) SecurityContextHolder.getContext()
//                .getAuthentication()
//                .getPrincipal();
//    }
//
//    public ExpenseResponse createExpense(ExpenseRequest request) {
//
//        User user = getCurrentUser();
//
//        Category category = categoryRepository
//                .findByIdAndUser(request.getCategoryId(), user)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Category not found"));
//
////        Expense expense = Expense.builder()
////                .title(request.getTitle())
////                .amount(request.getAmount())
////                .description(request.getDescription())
////                .expenseDate(request.getExpenseDate())
////                .paymentMethod(request.getPaymentMethod())
////                .category(category)
////                .user(user)
////                .build();
//
//        Expense savedExpense = expenseRepository.save(expense);
//
//        producer.publish(
//                ExpenseCreatedEvent.builder()
//                        .expenseId(savedExpense.getId())
//                        .userId(user.getId())
//                        .title(savedExpense.getTitle())
//                        .amount(savedExpense.getAmount())
//                        .expenseDate(savedExpense.getExpenseDate())
//                        .category(category.getName())
//                        .build()
//        );
//
//        return ExpenseMapper.toResponse(savedExpense);
//    }
//
//    public Page<ExpenseResponse> getAllExpenses(ExpenseFilterRequest filter) {
//
//        User user = getCurrentUser();
//
//        Sort sort = filter.getDirection().equalsIgnoreCase("desc")
//                ? Sort.by(filter.getSortBy()).descending()
//                : Sort.by(filter.getSortBy()).ascending();
//
//        Pageable pageable = PageRequest.of(
//                filter.getPage(),
//                filter.getSize(),
//                sort
//        );
//
//        Specification<Expense> specification =
//                ExpenseSpecification.belongsToUser(user);
//
//        if (filter.getCategoryId() != null) {
//
//            Category category = categoryRepository
//                    .findByIdAndUser(filter.getCategoryId(), user)
//                    .orElseThrow(() ->
//                            new ResourceNotFoundException("Category not found"));
//
//            specification = specification.and(
//                    ExpenseSpecification.hasCategory(category));
//        }
//
//        if (filter.getPaymentMethod() != null) {
//
//            specification = specification.and(
//                    ExpenseSpecification.hasPaymentMethod(
//                            filter.getPaymentMethod()));
//        }
//
//        if (filter.getStartDate() != null &&
//                filter.getEndDate() != null) {
//
//            specification = specification.and(
//                    ExpenseSpecification.expenseBetween(
//                            filter.getStartDate(),
//                            filter.getEndDate()));
//        }
//
//        if (filter.getMinAmount() != null &&
//                filter.getMaxAmount() != null) {
//
//            specification = specification.and(
//                    ExpenseSpecification.amountBetween(
//                            filter.getMinAmount(),
//                            filter.getMaxAmount()));
//        }
//
//        Page<Expense> page =
//                expenseRepository.findAll(specification, pageable);
//
//        return page.map(ExpenseMapper::toResponse);
//    }
//
//    public ExpenseResponse getExpense(Long id) {
//
//        User user = getCurrentUser();
//
//        Expense expense = expenseRepository
//                .findByIdAndUser(id, user)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Expense not found"));
//
//        return ExpenseMapper.toResponse(expense);
//    }
//
//    public ExpenseResponse updateExpense(Long id,
//                                         ExpenseRequest request) {
//
//        User user = getCurrentUser();
//
//        Expense expense = expenseRepository
//                .findByIdAndUser(id, user)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Expense not found"));
//
//        Category category = categoryRepository
//                .findByIdAndUser(request.getCategoryId(), user)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Category not found"));
//
//        expense.setTitle(request.getTitle());
//        expense.setAmount(request.getAmount());
//        expense.setDescription(request.getDescription());
//        expense.setExpenseDate(request.getExpenseDate());
//        expense.setPaymentMethod(request.getPaymentMethod());
//        expense.setCategory(category);
//
//        Expense updatedExpense = expenseRepository.save(expense);
//
//        return ExpenseMapper.toResponse(updatedExpense);
//    }
//
//    public void deleteExpense(Long id) {
//
//        User user = getCurrentUser();
//
//        Expense expense = expenseRepository
//                .findByIdAndUser(id, user)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException("Expense not found"));
//
//        expenseRepository.delete(expense);
//    }
//}
