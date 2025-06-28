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
public class ClipboardMessageFull implements ClipboardMessage {
    private String content;
    private ClipboardMessageMeta meta;
}
