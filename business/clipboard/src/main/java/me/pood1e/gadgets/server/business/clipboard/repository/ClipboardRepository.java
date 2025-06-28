package me.pood1e.gadgets.server.business.clipboard.repository;

import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessage;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessageFull;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessageMeta;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author pood1e
 */
public interface ClipboardRepository {
    Mono<Void> add(ClipboardMessageFull message);

    Flux<ClipboardMessageMeta> getTop(int limit);

    Mono<Void> remove(String id);

    Mono<ClipboardMessage> copy(String id);
}
