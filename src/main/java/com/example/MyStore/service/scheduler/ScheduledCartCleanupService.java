package com.example.MyStore.service.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public interface ScheduledCartCleanupService {



    void cleanupCarts();
}
