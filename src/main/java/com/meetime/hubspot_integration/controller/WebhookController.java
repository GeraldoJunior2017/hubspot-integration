// src/main/java/com/meetime/hubspot_integration/controller/WebhookController.java
package com.meetime.hubspot_integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meetime.hubspot_integration.config.OAuthProperties;
import com.meetime.hubspot_integration.dto.WebhookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks/contact")
public class WebhookController {

    private static final String SIGNATURE_HEADER = "X-HubSpot-Signature";
    private static final String HMAC_ALGO        = "HmacSHA256";

    private final OAuthProperties props;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Void> receive(
            @RequestHeader(SIGNATURE_HEADER) String signature,
            @RequestBody byte[] rawBody
    ) throws Exception {
        // 1) calcula HMAC do payload
        Mac mac = Mac.getInstance(HMAC_ALGO);
        mac.init(new SecretKeySpec(props.getClientSecret().getBytes(), HMAC_ALGO));
        String computed = Base64.getEncoder().encodeToString(mac.doFinal(rawBody));

        if (!computed.equals(signature)) {
            return ResponseEntity.status(401).build();
        }

        // 2) desserializa para o DTO
        WebhookEvent event = objectMapper.readValue(rawBody, WebhookEvent.class);

        // 3) só processa contact.creation
        if ("contact.creation".equalsIgnoreCase(event.getEventType())) {
            // TODO: Sua lógica de negócio aqui (ex.: persistir, enviar e-mail, etc.)
            System.out.println("Novo contato criado: " + event.getObjectId());
        }

        return ResponseEntity.ok().build();
    }
}
