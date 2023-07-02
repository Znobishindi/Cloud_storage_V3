package com.example.cloud_storage_v3.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthToken{
    @JsonProperty("auth-token")
    private String authToken;
}
