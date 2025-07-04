package org.example.stortiessearch.application.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class IncreasePostViewEvent {

    private Long postId;

    private Long userId;
}
