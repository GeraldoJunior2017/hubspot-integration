package com.meetime.hubspot_integration.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ContactRequest {
    private Map<String, String> properties;
}