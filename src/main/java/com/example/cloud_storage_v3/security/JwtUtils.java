package com.example.cloud_storage_v3.security;

import com.example.cloud_storage_v3.entity.Role;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {

    public static JwtAuth generate(Claims claims) {
        JwtAuth jwtInfoToken = new JwtAuth();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        String role = claims.get("roles", String.class);
        List<String> roles = new ArrayList<>();
        roles.add(role);

        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}