package com.softaria.amswebpagesmonitoring.config;

import com.softaria.amswebpagesmonitoring.service.WebPagesMonitoringService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация для мгновенного старта мониторинга после запуска приложения.
 * Далее приложение будет конфигурировать сообщение об изменениях страниц в 9 утра каждый день.
 * Убрать перед запуском в рабочей среде
 */
@Configuration
public class StartUpConfig {

    private final WebPagesMonitoringService monitoringService;

    public StartUpConfig(WebPagesMonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @Bean
    public ApplicationRunner runOnStartup() {
        return args -> monitoringService.monitorWebPages();
    }
}
