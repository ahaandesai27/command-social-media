package com.social.backend.security.rate_limiting;

import com.social.backend.payloads.RateLimitStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimiter {
    private static final int RATE_LIMIT = 100;
    private final AtomicInteger tokens = new AtomicInteger(RATE_LIMIT);

    @Scheduled(cron = "0 * * * * *")
    // 0 * * * * * means every minute
    private void refillBucket() {
        tokens.set(RATE_LIMIT);
    }

    public RateLimitStatus status() {
        return new RateLimitStatus(RATE_LIMIT, tokens.get());
    }

    public boolean consumeToken() {
        while (true) {
            int current = tokens.get();
            if (current == 0) return false;                                            // if not 0, goes to next statement
            if (tokens.compareAndSet(current, current-1)) return true;       // if compareAndSet fails, retries the loop
        }
    }
}
