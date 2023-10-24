package com.example.MyStore.service.scheduler;

import com.example.MyStore.service.scheduler.ScheduledCartCleanupService;
import com.example.MyStore.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledCartCleanupServiceImpl implements ScheduledCartCleanupService {

    private final UserService userService;

    public ScheduledCartCleanupServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron = "${schedulers.cron}")
    @Override
    public void cleanupCarts() {
        userService.getAllUsers().forEach(user ->  {
            if (user.getCart() != null && user.getCart().getCartItems().size() != 0) {
                userService.deleteAllCartItems(user.getUsername());
            }
        });
    }
}
