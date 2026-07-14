package com.ayush.expensetracker.controller;

import com.ayush.expensetracker.dto.budget.BudgetRequest;
import com.ayush.expensetracker.dto.budget.BudgetResponse;
import com.ayush.expensetracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Tag(name = "Budget", description = "Budget Management APIs")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @Operation(summary = "Create Budget")
    public ResponseEntity<BudgetResponse> createBudget(
            @Valid @RequestBody BudgetRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(budgetService.createBudget(request));
    }

    @GetMapping("/current")
    @Operation(summary = "Get Current Budget")
    public ResponseEntity<BudgetResponse> getCurrentBudget(
            @RequestParam Integer year,
            @RequestParam Month month) {

        return ResponseEntity.ok(
                budgetService.getCurrentBudget(year, month));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Budget")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request) {

        return ResponseEntity.ok(
                budgetService.updateBudget(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Budget")
    public ResponseEntity<Void> deleteBudget(
            @PathVariable Long id) {

        budgetService.deleteBudget(id);

        return ResponseEntity.noContent().build();
    }
}
