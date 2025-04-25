package com.meetime.hubspot_integration.controller;

import com.meetime.hubspot_integration.dto.TokenResponse;
import com.meetime.hubspot_integration.service.OAuthService;
import com.meetime.hubspot_integration.service.TokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de OAuth para o HubSpot.
 */
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oauthService;
    private final TokenStoreService tokenStore;

    /**
     * Gera a URL de autorização (já estava ok).
     */
    @GetMapping("/authorize")
    public String authorize() {
        return oauthService.buildAuthorizationUrl();
    }

    /**
     * Callback que recebe o código, faz a troca por token e salva o TokenResponse.
     */
    @GetMapping("/callback")
    public ResponseEntity<String> handleOAuthCallback(@RequestParam("code") String code) {
        // 1. Troca o código pelo TokenResponse
        TokenResponse resp = oauthService.exchangeForToken(code);

        // 2. Armazena access, refresh e expires_in
        tokenStore.save(resp);

        // 3. Retorno simples de confirmação
        return ResponseEntity.ok(
                "Token salvo com sucesso! Expira em "
                        + resp.getExpiresIn() + " segundos."
        );
    }
}
