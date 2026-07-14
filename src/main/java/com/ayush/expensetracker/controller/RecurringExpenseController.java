package com.ayush.expensetracker.controller;

import com.ayush.expensetracker.dto.recurring.RecurringExpenseRequest;
import com.ayush.expensetracker.dto.recurring.RecurringExpenseResponse;
import com.ayush.expensetracker.service.RecurringExpenseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring")
@RequiredArgsConstructor
@Tag(name = "Recurring Expenses",
        description = "Recurring Expense APIs")
public class RecurringExpenseController {

    private final RecurringExpenseService recurringExpenseService;

    @PostMapping
    public ResponseEntity<RecurringExpenseResponse> create(
            @Valid @RequestBody RecurringExpenseRequest request){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recurringExpenseService.create(request));

    }

    @GetMapping
    public ResponseEntity<List<RecurringExpenseResponse>> getAll(){

        return ResponseEntity.ok(
                recurringExpenseService.getAll());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id){

        recurringExpenseService.delete(id);

        return ResponseEntity.noContent().build();

    }
    @GetMapping("/{id}")
    public ResponseEntity<RecurringExpenseResponse> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                recurringExpenseService.getById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<RecurringExpenseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody RecurringExpenseRequest request) {

        return ResponseEntity.ok(
                recurringExpenseService.update(id, request));
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Recurring Controller Working");
    }


}
