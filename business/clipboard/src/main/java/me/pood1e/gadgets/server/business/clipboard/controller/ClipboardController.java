package me.pood1e.gadgets.server.business.clipboard.controller;

import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessage;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessageMeta;
import me.pood1e.gadgets.server.business.clipboard.service.ClipboardService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 提供接口
 *
 * @author pood1e
 */
@RestController
@RequestMapping("/clipboard")
public class ClipboardController {

    private final ClipboardService service;

    ClipboardController(ClipboardService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Mono<Void> add(@RequestBody ClipboardMessageRequest message) {
        return service.add(message);
    }

    @GetMapping("/recent")
    public Flux<ClipboardMessageMeta> getTop(@RequestParam(value = "limit", defaultValue = "10") int limit) {
        return service.getTop(limit);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> remove(@PathVariable("id") String id) {
        return service.remove(id);
    }

    @GetMapping("/{id}")
    public Mono<ClipboardMessage> copy(@PathVariable("id") String id) {
        return service.copy(id);
    }
}
