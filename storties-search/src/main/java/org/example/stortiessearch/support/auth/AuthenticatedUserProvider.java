package org.example.stortiessearch.support.auth;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.infrastructure.client.grpc.user.AuthGrpcClient;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserProvider {

    private final AuthGrpcClient authGrpcClient;

    private Long getCurrentUserId() {
        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()); // todo authentication 에 userId가 꼭 있어야함
    }

    public AuthenticatedUser getAuthenticatedUser() {
        return authGrpcClient.authenticate(getCurrentUserId());
    }

    public void checkAuthenticatedUserByUserId(Long userId) {
        authGrpcClient.authenticate(userId);
    }
}
