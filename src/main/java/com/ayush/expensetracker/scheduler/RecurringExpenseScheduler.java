package com.ayush.expensetracker.scheduler;

import com.ayush.expensetracker.entity.Expense;
import com.ayush.expensetracker.entity.RecurringExpense;
import com.ayush.expensetracker.enums.RecurringFrequency;
import com.ayush.expensetracker.repository.ExpenseRepository;
import com.ayush.expensetracker.repository.RecurringExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecurringExpenseScheduler {

    private final RecurringExpenseRepository recurringExpenseRepository;
    private final ExpenseRepository expenseRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void processRecurringExpenses() {

        LocalDate today = LocalDate.now();

        List<RecurringExpense> recurringExpenses =
                recurringExpenseRepository
                        .findByActiveTrueAndNextExecutionDate(today);

        for (RecurringExpense recurring : recurringExpenses) {

            Expense expense = Expense.builder()
                    .title(recurring.getTitle())
                    .amount(recurring.getAmount())
                    .description(recurring.getDescription())
                    .expenseDate(today)
                    .paymentMethod(recurring.getPaymentMethod())
                    .category(recurring.getCategory())
                    .user(recurring.getUser())
                    .build();

            expenseRepository.save(expense);

            // Kafka publishing is temporarily disabled

            recurring.setNextExecutionDate(
                    calculateNextDate(
                            recurring.getNextExecutionDate(),
                            recurring.getFrequency()
                    )
            );

            recurringExpenseRepository.save(recurring);

            log.info(
                    "Recurring expense created: {}",
                    expense.getTitle()
            );
        }
    }

    private LocalDate calculateNextDate(
            LocalDate currentDate,
            RecurringFrequency frequency
    ) {

        return switch (frequency) {
            case DAILY -> currentDate.plusDays(1);
            case WEEKLY -> currentDate.plusWeeks(1);
            case MONTHLY -> currentDate.plusMonths(1);
            case YEARLY -> currentDate.plusYears(1);
        };
    }
}
