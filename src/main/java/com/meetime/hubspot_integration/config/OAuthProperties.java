package com.meetime.hubspot_integration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "hubspot")
public class OAuthProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String authUrl;
    private String tokenUrl;
    private String scopes;
}