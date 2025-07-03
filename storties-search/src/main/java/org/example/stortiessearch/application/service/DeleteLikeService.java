package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.common.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.grpc.user.AuthenticatedUser;
import org.example.stortiessearch.persistence.CommandPostRepository;
import org.example.stortiessearch.persistence.QueryPostRepository;
import org.example.stortiessearch.persistence.model.PostLikeEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteLikeService {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    private final CommandPostRepository commandPostRepository;

    private final QueryPostRepository queryPostRepository;

    public void execute(Long postId) {
        AuthenticatedUser user = authenticatedUserProvider.getAuthenticatedUser();
        queryPostRepository.queryPostById(postId);
        PostLikeEntity postLikeEntity = queryPostRepository.queryLikeByPostIdAndUserId(postId, user.userId());

        commandPostRepository.deleteLikeById(postLikeEntity.getId());
    }
}
