package me.pood1e.gadgets.server.business.clipboard.repository.memory;

import com.github.benmanes.caffeine.cache.Expiry;
import jakarta.annotation.Nullable;

import java.time.Duration;

/**
 * @author pood1e
 */
class CustomExpiry<T> implements Expiry<String, T> {

    private final long expireTime;
    private final boolean refreshOnCopy;

    CustomExpiry(Duration expireTime, boolean refreshOnCopy) {
        this.expireTime = expireTime.toNanos();
        this.refreshOnCopy = refreshOnCopy;
    }

    @Override
    public long expireAfterCreate(@Nullable String key, @Nullable T value, long currentTime) {
        return expireTime;
    }

    @Override
    public long expireAfterUpdate(@Nullable String key, @Nullable T value, long currentTime, long currentDuration) {
        return refreshOnCopy ? expireTime : currentDuration;
    }

    @Override
    public long expireAfterRead(@Nullable String key, @Nullable T value, long currentTime, long currentDuration) {
        return currentDuration;
    }
}
