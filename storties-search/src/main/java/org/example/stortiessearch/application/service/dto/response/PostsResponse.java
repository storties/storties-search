package org.example.stortiessearch.application.service.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.stortiessearch.persistence.model.PostEntity;

@Getter
@AllArgsConstructor
public class PostsResponse {

    private final List<PostResponse> postResponses;

    public static PostsResponse from(Iterable<PostEntity> posts) {
        return new PostsResponse(((List<PostEntity>)posts).stream()
            .map(PostResponse::from)
            .toList());
    }
}
