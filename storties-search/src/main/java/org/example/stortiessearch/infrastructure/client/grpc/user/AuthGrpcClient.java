package org.example.stortiessearch.infrastructure.client.grpc.user;

import io.grpc.Channel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.springframework.stereotype.Component;
import storties.auth.grpc.AuthRequest;
import storties.auth.grpc.AuthServiceGrpc;

@Component
public class AuthGrpcClient {

    @GrpcClient("auth-service")
    private Channel channel;

    public AuthenticatedUser authenticate(Long userId) {
        AuthServiceGrpc.AuthServiceBlockingStub stub =
            AuthServiceGrpc.newBlockingStub(channel);

        AuthRequest request = AuthRequest.newBuilder()
            .setUserId(userId)
            .build();

        try {
            return AuthenticatedUser.from(stub.authenticate(request));
        } catch (StatusRuntimeException e) {
            if(e.getStatus() == Status.NOT_FOUND) {
                throw ErrorCodes.USER_NOT_FOUND.throwException();
            }
            throw ErrorCodes.INTERNAL_SERVER_ERROR.throwException();
        }
    }
}