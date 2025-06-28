package me.pood1e.gadgets.server.business.clipboard.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.time.Duration;

/**
 * @author pood1e
 */
@ConfigurationProperties(prefix = "clipboard.config")
@ConditionalOnProperty(name = "clipboard.storage", havingValue = "MEMORY")
@Component
@Getter
@Setter
public class MemoryStorageConfig implements StorageConfig {
    /**
     * 过期时间
     */
    private Duration expire = Duration.ofMinutes(10);

    /**
     * 最大条目约束
     */
    private Integer maxCount = 50;

    /**
     * 最大容量约束
     */
    private DataSize maxCapacity = DataSize.ofMegabytes(50);

    /**
     * 复制时刷新过期时间
     */
    private boolean refreshOnCopy = true;
}
