package com.ayush.expensetracker.consumer;

import com.ayush.expensetracker.event.ExpenseCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class BudgetConsumer {

    @KafkaListener(
            topics = "expense-created",
            groupId = "budget-group"
    )
    public void consume(ExpenseCreatedEvent event){

        log.info(
                "Checking budget after expense {}",
                event.getExpenseId());

    }
}
