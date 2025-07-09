package org.example.stortiessearch.application.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DecreasePostLikeEvent {

    private Long postId;

    private Long userId;
}
