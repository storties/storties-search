package org.example.stortiessearch.application.event;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.stortiessearch.data.persistence.post.model.PostEntity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostEvent {

    private Long id;

    private Long userId;

    private String title;

    private String content;

    private List<String> tags;

    private LocalDateTime updatedAt;

    private Boolean isPublished;

    public static UpdatePostEvent from(PostEntity entity) {
        return UpdatePostEvent.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .title(entity.getTitle())
            .content(entity.getContent())
            .tags(entity.getTags())
            .updatedAt(entity.getUpdatedAt())
            .isPublished(entity.getIsPublished())
            .build();
    }
}
