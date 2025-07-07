package org.example.stortiessearch.application.service;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.service.dto.response.PostsResponse;
import org.example.stortiessearch.data.persistence.post.QueryPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryPostsService {

    private final QueryPostRepository queryPostRepository;

    public PostsResponse execute(int page) {

        return PostsResponse.from(queryPostRepository.queryPostWithPaging(page));
    }
}
