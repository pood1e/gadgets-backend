package me.pood1e.gadgets.server.business.clipboard.config;

/**
 * 预计支持的存储方式
 *
 * @author pood1e
 */

public enum SupportedStorage {
    /**
     * caffeine存储
     */
    MEMORY,

    /**
     * 外部存储
     */
    REDIS,
    MYSQL
}
