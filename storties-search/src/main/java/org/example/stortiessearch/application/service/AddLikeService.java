package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.common.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.grpc.user.AuthenticatedUser;
import org.example.stortiessearch.infrastructure.persistence.CommandPostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddLikeService {

    private final CommandPostRepository commandPostRepository;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public void execute(Long postId) {
        AuthenticatedUser user = authenticatedUserProvider.getAuthenticatedUser();

        commandPostRepository.savePostLike(postId, user.userId());
    }
}
