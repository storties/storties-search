package org.example.stortiessearch.application.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PostViewEvent {
    private final Long postId;

    private final Long userId;
}
