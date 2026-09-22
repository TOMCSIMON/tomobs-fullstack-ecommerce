package com.tomobs.ecommerce.config;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiter {

    private final Map<String, List<Long>> requestLog = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 5;
    private static final long WINDOW_MS = 60_000;

    public boolean isAllowed(String userId) {
        long now = System.currentTimeMillis();
        List<Long> timestamps = requestLog.computeIfAbsent(userId, k -> new ArrayList<>());

        timestamps.removeIf(t -> now - t > WINDOW_MS);

        if (timestamps.size() >= MAX_REQUESTS) {
            return false;
        }

        timestamps.add(now);
        return true;
    }
}
