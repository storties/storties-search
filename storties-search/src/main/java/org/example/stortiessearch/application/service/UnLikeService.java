package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DecreasePostLikeEvent;
import org.example.stortiessearch.domain.post.QueryPostRepository;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.mq.producer.DecreasePostLikeProducer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnLikeService {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    private final DecreasePostLikeProducer decreasePostLikeProducer;

    private final QueryPostRepository queryPostRepository;

    public void execute(Long postId) {
        Long userId = authenticatedUserProvider.getCurrentUserId();
        queryPostRepository.queryPostById(postId);

        decreasePostLikeProducer.publish(DecreasePostLikeEvent
            .builder()
            .postId(postId)
            .userId(userId)
            .build());
    }
}
