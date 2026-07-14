package com.ayush.expensetracker.event;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseCreatedEvent {

    private Long expenseId;

    private Long userId;

    private String title;

    private BigDecimal amount;

    private LocalDate expenseDate;

    private String category;

}
