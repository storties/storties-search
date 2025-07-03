package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.PostEventListener;
import org.example.stortiessearch.application.event.PostViewEvent;
import org.example.stortiessearch.common.AuthenticatedUserProvider;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.infrastructure.grpc.user.AuthenticatedUser;
import org.example.stortiessearch.persistence.repository.PostJpaRepository;
import org.example.stortiessearch.application.service.dto.response.PostResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryPostService {

    private final PostJpaRepository postJpaRepository;

    private final PostEventListener postEventListener;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public PostResponse execute(Long postId) {
        AuthenticatedUser user = authenticatedUserProvider.getAuthenticatedUser();
        postEventListener.handlePostViewEvent(new PostViewEvent(postId, user.userId()));

        return PostResponse.from(postJpaRepository.findById(postId)
            .orElseThrow(ErrorCodes.POST_NOT_FOUND::throwException));
    }
}
