package org.example.stortiessearch.application.event;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.common.AuthenticatedUserProvider;
import org.example.stortiessearch.persistence.CommandPostRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventListener {

    private final CommandPostRepository commandPostRepository;
    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Async
    @EventListener
    public void handlePostViewEvent(PostViewEvent postViewEvent) {
        authenticatedUserProvider.checkAuthenticatedUserByUserId(postViewEvent.getUserId());

        commandPostRepository.savePostViewLog(postViewEvent.getUserId(), postViewEvent.getUserId());
    }
}
