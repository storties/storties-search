package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.infrastructure.event.PostViewEventListener;
import org.example.stortiessearch.application.event.IncreasePostViewEvent;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.example.stortiessearch.data.persistence.post.repository.PostJpaRepository;
import org.example.stortiessearch.application.service.dto.response.PostResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryPostService {

    private final PostJpaRepository postJpaRepository;

    private final PostViewEventListener postViewEventListener;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public PostResponse execute(Long postId) {
        AuthenticatedUser user = authenticatedUserProvider.getAuthenticatedUser();
        postViewEventListener.handlePostViewEvent(new IncreasePostViewEvent(postId, user.userId()));

        return PostResponse.from(postJpaRepository.findById(postId)
            .orElseThrow(ErrorCodes.POST_NOT_FOUND::throwException));
    }
}
