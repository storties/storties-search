package org.example.stortiessearch.domain.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.domain.post.model.PostEntity;
import org.example.stortiessearch.domain.post.model.PostLikeEntity;
import org.example.stortiessearch.domain.post.model.PostViewEntity;
import org.example.stortiessearch.domain.post.repository.PostJpaRepository;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.springframework.stereotype.Component;
import java.util.List;

import static org.example.stortiessearch.domain.persistence.post.model.QPostEntity.postEntity;
import static org.example.stortiessearch.domain.persistence.post.model.QPostLikeEntity.postLikeEntity;
import static org.example.stortiessearch.domain.persistence.post.model.QPostViewEntity.postViewEntity;

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
            .offset(page * 10)
            .limit(10)
            .fetch();
    }

    public Long queryViewCount(Long postId) {
        return queryFactory
            .select(postViewEntity.count())
            .from(postViewEntity)
            .groupBy(postViewEntity.post.id)
            .where(postViewEntity.post.id.eq(postId))
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

    public PostViewEntity queryViewByPostIdAndUserId(Long postId, Long userId) {
        return queryFactory
            .select(postViewEntity)
            .from(postViewEntity)
            .where(postViewEntity.post.id.eq(postId).and(postViewEntity.userId.eq(userId)))
            .fetchOne();
    }

    // todo 성능 최적화 전용 like,view poll 기능 생성
}
