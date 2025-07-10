package org.example.stortiessearch.application.service.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.example.stortiessearch.domain.post.model.PostEntity;
import org.example.stortiessearch.infrastructure.search.domain.post.document.PostDocument;

@Getter
@Builder
public class PostDetailResponse {
    private final Long id;

    private final Long userId;

    private final String title;

    private final String content;

    private final List<String> tags;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final Boolean isPublished;

    private final Long viewCount;

    private final Long likeCount;

    public static PostDetailResponse of(PostEntity post, Long likeCount, Long viewCount) {
       return PostDetailResponse.builder()
           .id(post.getId())
           .userId(post.getUserId())
           .title(post.getTitle())
           .content(post.getContent())
           .isPublished(post.getIsPublished())
           .tags(new ArrayList<>(post.getTags()))
           .createdAt(post.getCreatedAt())
           .updatedAt(post.getUpdatedAt())
           .likeCount(likeCount)
           .viewCount(viewCount)
           .build();
    }

    public static PostDetailResponse of(PostDocument post, Long likeCount, Long viewCount) {
        return PostDetailResponse.builder()
            .id(post.getId())
            .userId(post.getUserId())
            .title(post.getTitle())
            .content(post.getContent())
            .isPublished(post.getIsPublished())
            .tags(new ArrayList<>(post.getTags()))
            .createdAt(post.getCreatedAt())
            .updatedAt(post.getUpdatedAt())
            .likeCount(likeCount)
            .viewCount(viewCount)
            .build();
    }
}
