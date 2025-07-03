package org.example.stortiessearch.infrastructure.grpc.user;

import lombok.Builder;
import storties.auth.grpc.AuthResponse;

@Builder
public record AuthenticatedUser(Long userId, String email, String role) {

    static public AuthenticatedUser from(AuthResponse response) {
        return AuthenticatedUser.builder()
            .userId(response.getUserId())
            .email(response.getEmail())
            .role(response.getRole())
            .build();
    }
}

