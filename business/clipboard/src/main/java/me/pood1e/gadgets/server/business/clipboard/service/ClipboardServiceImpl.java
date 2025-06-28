package me.pood1e.gadgets.server.business.clipboard.service;

import me.pood1e.gadgets.server.business.clipboard.config.DeployConfig;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessage;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessageFull;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessageMeta;
import me.pood1e.gadgets.server.business.clipboard.repository.ClipboardRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @author pood1e
 */
@Service
public class ClipboardServiceImpl implements ClipboardService {

    private final ClipboardRepository repository;

    private final DeployConfig config;

    public ClipboardServiceImpl(ClipboardRepository repository, DeployConfig config) {
        this.repository = repository;
        this.config = config;
    }

    @Override
    public Mono<Void> add(ClipboardMessage message) {
        String content = message.getContent();
        ClipboardMessageMeta meta = ClipboardMessageMeta.builder()
                // TODO: use common id generator
                .id(UUID.randomUUID().toString())
                .preview(content.substring(0, Math.min(content.length(), config.getPreviewMaxLength())))
                .timestamp(System.currentTimeMillis())
                .bytes(content.getBytes(Charset.defaultCharset()).length)
                .length(content.length())
                .build();
        return repository.add(ClipboardMessageFull.builder()
                .meta(meta).content(content).build());
    }

    @Override
    public Flux<ClipboardMessageMeta> getTop(int limit) {
        return repository.getTop(limit);
    }

    @Override
    public Mono<Void> remove(String id) {
        return repository.remove(id);
    }

    @Override
    public Mono<ClipboardMessage> copy(String id) {
        return repository.copy(id);
    }
}
