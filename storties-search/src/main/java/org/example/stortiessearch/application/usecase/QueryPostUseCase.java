package org.example.stortiessearch.application.usecase;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostViewEvent;
import org.example.stortiessearch.common.annotations.UseCase;
import org.example.stortiessearch.domain.post.model.PostEntity;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.infrastructure.cache.service.GetLikeCountService;
import org.example.stortiessearch.infrastructure.cache.service.GetViewCountService;
import org.example.stortiessearch.domain.post.repository.PostJpaRepository;
import org.example.stortiessearch.application.usecase.dto.response.PostDetailResponse;
import org.example.stortiessearch.infrastructure.mq.producer.IncreasePostViewProducer;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryPostUseCase {

    private final PostJpaRepository postJpaRepository;

    private final IncreasePostViewProducer increasePostViewProducer;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    private final GetViewCountService getViewCountService;

    private final GetLikeCountService getLikeCountService;

    public PostDetailResponse execute(Long postId) { // todo 테스트 미완
        Long userId = authenticatedUserProvider.getCurrentUserId();
        increasePostViewProducer.publish(new IncreasePostViewEvent(postId, userId));

        Long viewCount = getViewCountService.getViewCounts(List.of(postId)).get(postId);
        Long likeCounts = getLikeCountService.getLikeCounts(List.of(postId)).get(postId);

        PostEntity post = postJpaRepository.findById(postId)
            .orElseThrow(ErrorCodes.POST_NOT_FOUND::throwException);

        return PostDetailResponse.of(post, likeCounts, viewCount);
    }
}
