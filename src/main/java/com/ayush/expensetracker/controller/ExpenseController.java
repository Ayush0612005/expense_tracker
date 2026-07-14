package com.ayush.expensetracker.controller;

import com.ayush.expensetracker.dto.expense.ExpenseFilterRequest;
import com.ayush.expensetracker.dto.expense.ExpenseRequest;
import com.ayush.expensetracker.dto.expense.ExpenseResponse;
import com.ayush.expensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "Expense Management APIs")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Create Expense")
    public ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody ExpenseRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.createExpense(request));
    }

    @GetMapping
    @Operation(summary = "Get Expenses with Pagination & Filters")
    public ResponseEntity<Page<ExpenseResponse>> getAllExpenses(
            @ModelAttribute ExpenseFilterRequest filter) {

        return ResponseEntity.ok(
                expenseService.getAllExpenses(filter)
        );
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get Expense By Id")
    public ResponseEntity<ExpenseResponse> getExpense(
            @PathVariable Long id) {

        return ResponseEntity.ok(expenseService.getExpense(id));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Update Expense")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequest request) {

        return ResponseEntity.ok(
                expenseService.updateExpense(id, request));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Expense")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id) {

        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
}
