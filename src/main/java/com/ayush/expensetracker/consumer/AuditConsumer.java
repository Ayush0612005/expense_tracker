package com.ayush.expensetracker.consumer;

import com.ayush.expensetracker.event.ExpenseCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class AuditConsumer {

    @KafkaListener(
            topics = "expense-created",
            groupId = "audit-group"
    )
    public void consume(ExpenseCreatedEvent event){

        log.info("""
                ==========================
                AUDIT EVENT
                Expense ID : {}
                User ID    : {}
                Amount      : {}
                Category    : {}
                ==========================
                """,
                event.getExpenseId(),
                event.getUserId(),
                event.getAmount(),
                event.getCategory());

    }
}
