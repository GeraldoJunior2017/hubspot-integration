package com.meetime.hubspot_integration.service;

import com.meetime.hubspot_integration.dto.ContactRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class HubSpotService {

    private final WebClient webClient;

    /**
     * Cria um contato no HubSpot.
     *
     * @param token   Bearer token válido para autenticação
     * @param request Payload mapeado pelo DTO ContactRequest
     * @return corpo da resposta do HubSpot como String
     */
    public String createContact(String token, ContactRequest request) {
        return webClient.post()
                .uri("/crm/v3/objects/contacts")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
