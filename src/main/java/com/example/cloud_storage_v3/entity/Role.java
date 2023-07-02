package com.example.cloud_storage_v3.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public enum Role  implements GrantedAuthority {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String vale;

    @Override
    public String getAuthority() {
        return vale;
    }
}
