package com.ayush.expensetracker.controller;

import com.ayush.expensetracker.dto.report.*;
import com.ayush.expensetracker.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Reports")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/category")
    @Operation(summary = "Category Report")
    public ResponseEntity<List<CategoryReportResponse>>
    category(
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                reportService.getCategoryReport(year, month));
    }

    @GetMapping("/monthly")
    @Operation(summary = "Monthly Report")
    public ResponseEntity<List<MonthlyReportResponse>>
    monthly(
            @RequestParam int year) {

        return ResponseEntity.ok(
                reportService.getMonthlyReport(year));
    }

    @GetMapping("/yearly")
    @Operation(summary = "Yearly Report")
    public ResponseEntity<List<YearlyReportResponse>>
    yearly() {

        return ResponseEntity.ok(
                reportService.getYearlyReport());
    }

    @GetMapping("/summary")
    @Operation(summary = "Summary Report")
    public ResponseEntity<SummaryReportResponse>
    summary() {

        return ResponseEntity.ok(
                reportService.getSummaryReport());
    }
}
