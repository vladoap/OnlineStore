package com.example.MyStore.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface ScheduledCartCleanupService {



    void cleanupCarts();
}
