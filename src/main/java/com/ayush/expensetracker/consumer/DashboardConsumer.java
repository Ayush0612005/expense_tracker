package com.ayush.expensetracker.consumer;

import com.ayush.expensetracker.event.ExpenseCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.stereotype.Component;

@Slf4j
//@Component
@RequiredArgsConstructor
public class DashboardConsumer {

    private final CacheManager cacheManager;

    @KafkaListener(
            topics = "expense-created",
            groupId = "dashboard-group"
    )
    public void consume(ExpenseCreatedEvent event){

        if(cacheManager.getCache("dashboard") != null){
            cacheManager.getCache("dashboard").clear();
        }

        log.info(
                "Dashboard cache cleared after expense {}",
                event.getExpenseId());

    }
}
