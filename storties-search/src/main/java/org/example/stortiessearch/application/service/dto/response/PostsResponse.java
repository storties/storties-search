package org.example.stortiessearch.application.service.dto.response;

import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.stortiessearch.data.search.post.document.PostDocument;

@Getter
@AllArgsConstructor
public class PostsResponse {

    private final List<PostDetailResponse> postDetailResponse;

    public static PostsResponse of(List<PostDocument> posts, HashMap<Long, Long> postLikes,
                                   HashMap<Long, Long> postViews) {

        return new PostsResponse(posts.stream().map(post -> {
                Long postId = post.getId();
                long likeCount = postLikes.getOrDefault(postId, 0L);
                long viewCount = postViews.getOrDefault(postId, 0L);
                return PostDetailResponse.of(post, likeCount, viewCount);
            })
            .toList()
        );
    }
}
