package org.example.stortiessearch.application.event;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stortiessearch.data.persistence.model.PostEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostEvent {

    private Long id;

    private Long userId;

    private String title;

    private String content;

    private List<String> tags;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isPublished;

    public static CreatePostEvent from(PostEntity entity) {
        return CreatePostEvent.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .title(entity.getTitle())
            .content(entity.getContent())
            .tags(entity.getTags())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .isPublished(entity.getIsPublished())
            .build();
    }
}
