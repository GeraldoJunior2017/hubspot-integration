package com.meetime.hubspot_integration.service;

import com.meetime.hubspot_integration.dto.TokenResponse;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenStoreService {

    private TokenResponse tokenResponse;
    private Instant expiryTime;

    /**
     * Armazena o TokenResponse completo e calcula o instante de expiração.
     */
    public void save(TokenResponse resp) {
        this.tokenResponse = resp;
        this.expiryTime = Instant.now().plusSeconds(resp.getExpiresIn());
    }

    /** Indica se há token e ele ainda não expirou */
    public boolean hasToken() {
        return tokenResponse != null && Instant.now().isBefore(expiryTime);
    }

    /** Retorna o access_token atual */
    public String getAccessToken() {
        return tokenResponse.getAccessToken();
    }

    /** Retorna o refresh_token */
    public String getRefreshToken() {
        return tokenResponse.getRefreshToken();
    }

    /** Indica se o token já expirou */
    public boolean isExpired() {
        return tokenResponse == null || Instant.now().isAfter(expiryTime);
    }

    /** Retorna o objeto completo, caso precise */
    public TokenResponse getTokenResponse() {
        return tokenResponse;
    }
}
