package org.example.stortiessearch.application.usecase.dto.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.stortiessearch.domain.post.model.PostEntity;
import org.example.stortiessearch.infrastructure.search.domain.post.document.PostDocument;

@Getter
@AllArgsConstructor
public class PostsResponse {

    private final List<PostDetailResponse> postDetailResponse;

    public static PostsResponse ofDocument(List<PostDocument> posts, Map<Long, Long> postLikes,
                                           Map<Long, Long> postViews) {

        return new PostsResponse(posts.stream().map(post -> {
                Long postId = post.getId();
                long likeCount = postLikes.getOrDefault(postId, 0L);
                long viewCount = postViews.getOrDefault(postId, 0L);
                return PostDetailResponse.of(post, likeCount, viewCount);
            }).toList()
        );
    }

    public static PostsResponse ofEntity(List<PostEntity> posts, Map<Long, Long> postLikes,
                                           Map<Long, Long> postViews) {

        return new PostsResponse(posts.stream().map(post -> {
                Long postId = post.getId();
                long likeCount = postLikes.getOrDefault(postId, 0L);
                long viewCount = postViews.getOrDefault(postId, 0L);
                return PostDetailResponse.of(post, likeCount, viewCount);
            }).toList()
        );
    }
}
