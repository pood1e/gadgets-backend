package me.pood1e.gadgets.server.business.clipboard.repository.memory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.Weigher;
import lombok.extern.slf4j.Slf4j;
import me.pood1e.gadgets.server.business.clipboard.config.MemoryStorageConfig;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessage;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessageFull;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessageMeta;
import me.pood1e.gadgets.server.business.clipboard.repository.ClipboardRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author pood1e
 */
@ConditionalOnProperty(name = "clipboard.storage", havingValue = "MEMORY")
@Repository
@Slf4j
public class ClipboardMemoryRepository implements ClipboardRepository {

    private final Cache<String, ClipboardMessageFull> cache;

    public ClipboardMemoryRepository(MemoryStorageConfig config) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        if (config.getExpire() != null) {
            caffeine.expireAfter(new CustomExpiry<ClipboardMessageFull>(config.getExpire(), config.isRefreshOnCopy()));
        }
        if (config.getMaxCapacity() != null) {
            caffeine.weigher((Weigher<String, ClipboardMessageFull>) (_, value) -> value.getMeta().getBytes())
                    .maximumWeight(config.getMaxCapacity().toBytes());
        } else if (config.getMaxCount() != null) {
            caffeine.maximumSize(config.getMaxCount());
        }
        this.cache = caffeine
                .evictionListener((RemovalListener<String, ClipboardMessageFull>) (key, _, cause) -> log.info("message evicted from cache: {} ,cause: {}", key, cause))
                .build();
    }

    @Override
    public Mono<Void> add(ClipboardMessageFull message) {
        return Mono.fromRunnable(() -> cache.put(message.getMeta().getId(), message));
    }

    @Override
    public Flux<ClipboardMessageMeta> getTop(int limit) {
        return Flux.defer(() -> cache.policy().eviction()
                        .map(eviction -> Flux.fromIterable(eviction.hottest(limit).entrySet()))
                        .orElseGet(Flux::empty))
                .map(entry -> entry.getValue().getMeta());
    }

    @Override
    public Mono<Void> remove(String id) {
        return Mono.fromRunnable(() -> cache.invalidate(id));
    }

    @Override
    public Mono<ClipboardMessage> copy(String id) {
        return Mono.fromCallable(() -> {
            ClipboardMessageFull message = cache.asMap().computeIfPresent(id, (_, v) -> {
                v.getMeta().setTimestamp(System.currentTimeMillis());
                return v;
            });
            return () -> {
                assert message != null;
                return message.getContent();
            };
        });
    }
}
