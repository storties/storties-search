package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.support.auth.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.example.stortiessearch.data.persistence.CommandPostRepository;
import org.example.stortiessearch.data.persistence.QueryPostRepository;
import org.example.stortiessearch.data.persistence.model.PostLikeEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnLikeService {

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
