package com.thekuzea.experimental.config;

import java.util.Locale;
import javax.validation.Validator;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class LocaleConfig {

    @Bean
    public Validator validator() {
        final LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource());

        return factory;
    }

    private MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/validation");
        messageSource.setDefaultLocale(Locale.ENGLISH);

        return messageSource;
    }
}
