// src/main/java/com/social/backend/security/rate_limiting/RateLimiter.java
package com.social.backend.security.rate_limiting;

import com.social.backend.payloads.RateLimitStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimiter {

    private static final int RATE_LIMIT = 100;
    private static final long INACTIVITY_LIMIT_MS = 10 * 60 * 1000; // 10 min

    private static class Bucket {
        AtomicInteger tokens = new AtomicInteger(RATE_LIMIT);
        volatile long lastAccess = System.currentTimeMillis();
    }

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Bucket>> buckets =
            new ConcurrentHashMap<>();

    @Scheduled(cron = "0 * * * * *")
    private void refillBuckets() {
        buckets.forEach((ip, userMap) ->
                userMap.forEach((user, bucket) ->
                        bucket.tokens.set(RATE_LIMIT)
                )
        );
    }

    @Scheduled(fixedRate = 5 * 60 * 1000)
    private void cleanupBuckets() {
        long now = System.currentTimeMillis();

        buckets.forEach((ip, userMap) -> {
            userMap.forEach((user, bucket) -> {
                if (now - bucket.lastAccess > INACTIVITY_LIMIT_MS) {
                    userMap.remove(user);
                }
            });

            if (userMap.isEmpty()) {
                buckets.remove(ip);
            }
        });
    }

    public RateLimitStatus status(String ip, String username) {
        Bucket bucket = buckets
                .computeIfAbsent(ip, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(username, k -> new Bucket());

        bucket.lastAccess = System.currentTimeMillis();
        return new RateLimitStatus(RATE_LIMIT, bucket.tokens.get());
    }

    public boolean consumeToken(String ip, String username) {
        ConcurrentHashMap<String, Bucket> userMap =
                buckets.computeIfAbsent(ip, k -> new ConcurrentHashMap<>());

        Bucket bucket = userMap.computeIfAbsent(username, k -> new Bucket());
        bucket.lastAccess = System.currentTimeMillis();

        while (true) {
            int current = bucket.tokens.get();
            if (current == 0) return false;
            if (bucket.tokens.compareAndSet(current, current - 1)) return true;
        }
    }
}