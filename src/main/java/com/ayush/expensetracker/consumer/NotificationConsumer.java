package com.ayush.expensetracker.consumer;

import com.ayush.expensetracker.event.ExpenseCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Component;

@Slf4j
//@Component
public class NotificationConsumer {

    @KafkaListener(
            topics = "expense-created",
            groupId = "notification-group"
    )
    public void consume(ExpenseCreatedEvent event){

        log.info(
                "Notification would be sent for expense {}",
                event.getExpenseId());

    }
}

