package org.example.stortiessearch.application.service.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.example.stortiessearch.persistence.model.PostEntity;

@Getter
@Builder
public class PostResponse {
    private final Long id;

    private final Long userId;

    private final String title;

    private final String content;

    private final List<String> tags;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final Boolean isPublished;

    public static PostResponse from(PostEntity postEntity) {
       return PostResponse.builder()
            .id(postEntity.getId())
            .title(postEntity.getTitle())
            .content(postEntity.getContent())
            .isPublished(postEntity.getIsPublished())
            .tags(postEntity.getTags())
            .createdAt(postEntity.getCreatedAt())
            .updatedAt(postEntity.getUpdatedAt())
            .build();
    }
}
