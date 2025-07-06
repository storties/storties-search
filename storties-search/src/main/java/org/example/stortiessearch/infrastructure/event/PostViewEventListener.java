package org.example.stortiessearch.infrastructure.event;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.support.auth.AuthenticatedUserProvider;
import org.example.stortiessearch.application.event.IncreasePostViewEvent;
import org.example.stortiessearch.data.persistence.CommandPostRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostViewEventListener {

    private final CommandPostRepository commandPostRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Async
    @EventListener
    public void handlePostViewEvent(IncreasePostViewEvent increasePostViewEvent) {
        authenticatedUserProvider.checkAuthenticatedUserByUserId(increasePostViewEvent.getUserId());

        commandPostRepository.savePostViewLog(increasePostViewEvent.getUserId(), increasePostViewEvent.getUserId());
    }
}
