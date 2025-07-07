package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.data.persistence.QueryPostRepository;
import org.example.stortiessearch.data.persistence.model.PostLikeEntity;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.example.stortiessearch.data.persistence.CommandPostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddLikeService {

    private final CommandPostRepository commandPostRepository;

    private final QueryPostRepository queryPostRepository;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public void execute(Long postId) {
        AuthenticatedUser user = authenticatedUserProvider.getAuthenticatedUser();

        PostLikeEntity postLikeEntity = queryPostRepository.queryLikeByPostIdAndUserId(postId, user.userId());

        if(postLikeEntity != null) {
            throw ErrorCodes.ALREADY_LIKED.throwException();
        }

        commandPostRepository.savePostLike(postId, user.userId());
    }
}
