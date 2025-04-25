// src/main/java/com/meetime/hubspot_integration/exception/RestExceptionHandler.java
package com.meetime.hubspot_integration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Trata exceções globais para respostas HTTP padronizadas.
 */
@ControllerAdvice
public class RestExceptionHandler {

    /**
     * Mapeia erros HTTP (4xx/5xx) retornados pelo WebClient e repassa status e corpo.
     * Inclui também os 429 (Too Many Requests) vindos do HubSpot.
     */
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientError(WebClientResponseException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getResponseBodyAsString());
    }

    /**
     * Captura qualquer outra exceção e retorna 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + ex.getMessage());
    }
}
