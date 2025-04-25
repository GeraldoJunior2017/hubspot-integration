// src/main/java/com/meetime/hubspot_integration/controller/ContactController.java
package com.meetime.hubspot_integration.controller;

import com.meetime.hubspot_integration.dto.ContactRequest;
import com.meetime.hubspot_integration.service.HubSpotService;
import com.meetime.hubspot_integration.service.TokenStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final HubSpotService hubSpotService;
    private final TokenStoreService tokenStore;

    /**
     * POST /contacts
     * - Header: Authorization: Bearer {qualquer-coisa}  (só para passar pela segurança)
     * - Body: ContactRequest
     */
    @PostMapping
    public ResponseEntity<String> create(
            @RequestHeader("Authorization") String ignoredBearer,
            @RequestBody ContactRequest request) {

        // 1) Verifica se há token válido salvo
        if (!tokenStore.hasToken()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Token ausente ou expirado. Execute o fluxo OAuth em /oauth/authorize");
        }

        // 2) Pega o access token do TokenStoreService
        String token = tokenStore.getAccessToken();

        // 3) Chama o serviço que faz a requisição ao HubSpot
        String resp = hubSpotService.createContact(token, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
