package com.ucb.amae.vault.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucb.amae.vault.model.VaultEntry;

import java.util.List;

public class JsonSerializationService {

    private final ObjectMapper objectMapper;

    public JsonSerializationService() {
        this.objectMapper = new ObjectMapper();
    }

    public String toJson(List<VaultEntry> entries) throws JsonProcessingException {
        return objectMapper.writeValueAsString(entries);
    }

    public List<VaultEntry> fromJson(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, new TypeReference<List<VaultEntry>>() {});
    }
}
