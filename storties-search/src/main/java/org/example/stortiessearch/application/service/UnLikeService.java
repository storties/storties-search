package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.example.stortiessearch.data.persistence.post.CommandPostRepository;
import org.example.stortiessearch.data.persistence.post.QueryPostRepository;
import org.example.stortiessearch.data.persistence.post.model.PostLikeEntity;
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
