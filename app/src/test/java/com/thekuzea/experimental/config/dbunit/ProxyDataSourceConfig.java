package com.thekuzea.experimental.config.dbunit;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.thekuzea.experimental.config.dbunit.resource.ProxyDataSourceBeanPostProcessor;

@TestConfiguration
@Profile("dbunit")
public class ProxyDataSourceConfig {

    @Bean
    public ProxyDataSourceBeanPostProcessor proxyTestDataSourceBeanPostProcessor() {
        return new ProxyDataSourceBeanPostProcessor();
    }
}
