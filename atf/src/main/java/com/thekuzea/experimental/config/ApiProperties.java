package com.thekuzea.experimental.config;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.thekuzea.experimental.domain.enums.RestApiType;

@Configuration
@ConfigurationProperties(prefix = "experimental.api")
@Setter
@Getter
public class ApiProperties {

    private String protocol;

    private String host;

    private int port;

    private String path;

    private Map<RestApiType, String> subPaths;
}
