package org.example.stortiessearch.application.usecase;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DecreasePostLikeEvent;
import org.example.stortiessearch.common.annotations.UseCase;
import org.example.stortiessearch.domain.post.QueryPostRepository;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.mq.producer.DecreasePostLikeProducer;

@UseCase
@RequiredArgsConstructor
public class UnLikeUseCase {

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
