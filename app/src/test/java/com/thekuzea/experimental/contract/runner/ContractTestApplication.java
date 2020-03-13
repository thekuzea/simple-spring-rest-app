package com.thekuzea.experimental.contract.runner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.thekuzea.experimental.config.PersistenceConfig;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class
})
@ComponentScan(
        basePackages = {
                "com.thekuzea.experimental.config",
                "com.thekuzea.experimental.api",
                "com.thekuzea.experimental.infrastructure.security",
                "com.thekuzea.experimental.support.util"
        },
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {PersistenceConfig.class}
                )
        }
)
public class ContractTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractTestApplication.class, args);
    }
}
