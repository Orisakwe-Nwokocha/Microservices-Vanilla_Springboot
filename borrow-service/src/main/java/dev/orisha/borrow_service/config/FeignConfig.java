package dev.orisha.borrow_service.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "dev.orisha.borrow_service.services.utils")
public class FeignConfig {
}
