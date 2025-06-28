package me.pood1e.gadgets.server.business.clipboard.domain;

import lombok.*;

/**
 * @author pood1e
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClipboardMessageMeta {
    private String id;
    private String preview;
    private int length;
    private int bytes;
    private long timestamp;
}
