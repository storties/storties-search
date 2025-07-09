package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostLikeEvent;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.mq.producer.IncreasePostLikeProducer;
import org.springframework.stereotype.Service;

@Service // todo Usecase로 변경하자
@RequiredArgsConstructor
public class AddLikeService {

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
