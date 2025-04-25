package com.meetime.hubspot_integration.service;

import com.meetime.hubspot_integration.config.OAuthProperties;
import com.meetime.hubspot_integration.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProperties props;
    private final WebClient webClient;

    /**
     * Monta a URL de autorização completa para iniciar o fluxo OAuth.
     */
    public String buildAuthorizationUrl() {
        return props.getAuthUrl()
                + "?client_id="    + props.getClientId()
                + "&redirect_uri=" + props.getRedirectUri()
                + "&scope="        + props.getScopes()
                + "&response_type=code";
    }

    /**
     * Troca o código de autorização por um TokenResponse.
     */
    public TokenResponse exchangeForToken(String code) {
        String form = "grant_type=authorization_code"
                + "&code=" + code
                + "&client_id=" + props.getClientId()
                + "&client_secret=" + props.getClientSecret()
                + "&redirect_uri=" + props.getRedirectUri();

        return webClient.post()
                .uri(props.getTokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(form)
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .block();
    }
}
