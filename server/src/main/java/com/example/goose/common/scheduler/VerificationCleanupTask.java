package com.example.goose.common.scheduler;

import com.example.goose.iGoose.auth.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class VerificationCleanupTask {

    private final AuthMapper authMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void cleanupExpiredUsers() {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, -3);

        authMapper.deleteExpiredUsers(calendar.getTime());
    }
}
