package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.service.dto.response.PostsResponse;
import org.example.stortiessearch.infrastructure.persistence.QueryPostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryPostsService {

    private final QueryPostRepository queryPostRepository;

    public PostsResponse execute(int page) {

        return PostsResponse.from(queryPostRepository.queryPostWithPaging(page));
    }
}
