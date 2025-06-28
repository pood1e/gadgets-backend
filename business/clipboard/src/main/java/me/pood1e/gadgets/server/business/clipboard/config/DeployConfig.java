package me.pood1e.gadgets.server.business.clipboard.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

/**
 * @author pood1e
 */
@ConfigurationProperties(prefix = "clipboard")
@Component
@Getter
@Setter
public class DeployConfig {
    /**
     * 预览字符串长度
     */
    private int previewMaxLength = 50;

    /**
     * 最大剪贴板消息限制
     */
    private DataSize contentMaxSize = DataSize.ofMegabytes(5);

    /**
     * 存储
     */
    private SupportedStorage storage;
}
