package org.example.stortiessearch.application.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.service.dto.response.PostsResponse;
import org.example.stortiessearch.data.persistence.post.QueryPostRepository;
import org.example.stortiessearch.data.persistence.post.model.PostEntity;
import org.example.stortiessearch.infrastructure.cache.service.GetLikeCountService;
import org.example.stortiessearch.infrastructure.cache.service.GetViewCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryPostsService {

    private final QueryPostRepository queryPostRepository;

    private final GetViewCountService getViewCountService;

    private final GetLikeCountService getLikeCountService;

    public PostsResponse execute(int page) {
        List<PostEntity> post = queryPostRepository.queryPostWithPaging(page);
        List<Long> postIds = post.stream().map(PostEntity::getId).toList();

        Map<Long,Long> likeCounts = getLikeCountService.getLikeCounts(postIds);
        Map<Long, Long> viewCounts = getViewCountService.getViewCounts(postIds);

        return PostsResponse.ofEntity(post, likeCounts, viewCounts);
    }
}
