package com.cgm.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 附加配置
 *
 * @author cgm
 */
@Data
@Configuration
@ConfigurationProperties("extra")
public class ExtraConfig {
    /**
     * 默认用户头像的地址
     */
    private String defaultAvatar;
}
