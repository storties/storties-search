package org.example.stortiessearch.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.persistence.model.PostEntity;
import org.example.stortiessearch.persistence.model.PostLikeEntity;
import org.example.stortiessearch.persistence.repository.PostJpaRepository;
import org.springframework.stereotype.Component;
import java.util.List;

import static org.example.stortiessearch.persistence.model.QPostEntity.postEntity;
import static org.example.stortiessearch.persistence.model.QPostLikeEntity.postLikeEntity;
import static org.example.stortiessearch.persistence.model.QPostViewLogEntity.postViewLogEntity;

@Component
@RequiredArgsConstructor
public class QueryPostRepository {

    private final PostJpaRepository postJpaRepository;

    private final JPAQueryFactory queryFactory;

    public PostEntity queryPostById(Long postId) {
        return postJpaRepository.findById(postId)
            .orElseThrow(ErrorCodes.POST_NOT_FOUND::throwException);
    }

    public List<PostEntity> queryAllPost() {
        return (List<PostEntity>) postJpaRepository.findAll();
    }

    public List<PostEntity> queryPostWithPaging(long page) {
        return queryFactory
            .select(postEntity)
            .from(postEntity)
            .offset((page-1) * 10)
            .limit(10)
            .fetch();
    }

    public Long queryViewCount(Long postId) {
        return queryFactory
            .select(postViewLogEntity.count())
            .from(postViewLogEntity)
            .groupBy(postViewLogEntity.post.id)
            .where(postViewLogEntity.post.id.eq(postId))
            .fetchOne();
    }

    public Long queryLikeCount(Long postId) {
        return queryFactory
            .select(postLikeEntity.count())
            .from(postLikeEntity)
            .groupBy(postLikeEntity.post.id)
            .where(postLikeEntity.post.id.eq(postId))
            .fetchOne();
    }

    public PostLikeEntity queryLikeByPostIdAndUserId(Long postId, Long userId) {
        return queryFactory
            .select(postLikeEntity)
            .from(postLikeEntity)
            .where(postLikeEntity.post.id.eq(postId).and(postLikeEntity.userId.eq(userId)))
            .fetchOne();
    }

    // todo 성능 최적화 전용 like,view poll 기능 생성
}
