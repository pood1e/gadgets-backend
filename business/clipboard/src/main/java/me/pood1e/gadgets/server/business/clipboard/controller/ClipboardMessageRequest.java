package me.pood1e.gadgets.server.business.clipboard.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import me.pood1e.gadgets.server.business.clipboard.domain.ClipboardMessage;
import me.pood1e.gadgets.server.business.common.validator.MaxDataSize;

/**
 * @author pood1e
 */
@Getter
@Setter
public class ClipboardMessageRequest implements ClipboardMessage {
    @MaxDataSize("@deployConfig.contentMaxSize")
    @NotBlank
    private String content;
}
