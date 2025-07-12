package org.example.stortiessearch.application.usecase;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostLikeEvent;
import org.example.stortiessearch.common.annotations.UseCase;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.mq.kafka.event.like.increase.IncreasePostLikeProducer;

@UseCase
@RequiredArgsConstructor
public class AddLikeUseCase {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    private final IncreasePostLikeProducer increasePostLikeProducer;

    public void execute(Long postId) {
        Long userId = authenticatedUserProvider.getCurrentUserId();

        increasePostLikeProducer.publish(IncreasePostLikeEvent
            .builder()
            .postId(postId)
            .userId(userId)
            .build());
    }
}
