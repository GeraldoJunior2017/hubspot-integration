package com.meetime.hubspot_integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient hubSpotWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.hubapi.com")
                .build();
    }
}