package com.meetime.hubspot_integration.dto;

import lombok.Data;
import java.util.List;

@Data
public class WebhookEvent {
    private String subscriptionType;
    private String eventId;
    private String eventType;
    private String occurredAt;
    private List<Object> objectId;
}