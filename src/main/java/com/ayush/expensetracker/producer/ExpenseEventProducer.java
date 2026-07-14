package com.ayush.expensetracker.producer;

import com.ayush.expensetracker.event.ExpenseCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Component;

@RequiredArgsConstructor
// @Component
public class ExpenseEventProducer {

    private final KafkaTemplate<String, ExpenseCreatedEvent> kafkaTemplate;

    public void publish(ExpenseCreatedEvent event) {

        kafkaTemplate.send(
                "expense-created",
                event
        );
    }
}
