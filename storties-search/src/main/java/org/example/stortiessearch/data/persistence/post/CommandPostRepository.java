package org.example.stortiessearch.data.persistence.post;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.data.persistence.post.model.PostLikeEntity;
import org.example.stortiessearch.data.persistence.post.model.PostViewEntity;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.data.persistence.post.model.PostEntity;
import org.example.stortiessearch.data.persistence.post.repository.PostJpaRepository;
import org.example.stortiessearch.data.persistence.post.repository.PostLikeJpaRepository;
import org.example.stortiessearch.data.persistence.post.repository.PostViewLogJpaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandPostRepository {

    private final PostJpaRepository postJpaRepository;

    private final PostViewLogJpaRepository postViewLogJpaRepository;

    private final PostLikeJpaRepository postLikeJpaRepository;

    public PostEntity savePost(PostEntity postEntity) {
        return postJpaRepository.save(postEntity);
    }

    public void deletePostByPostId(Long postId) {
        postJpaRepository.deleteById(postId);
    }

    public void deleteLikeById(Long id) {
        postJpaRepository.deleteById(id);
    }

    public void savePostViewLog(Long postId, Long userId) {
        PostEntity post = postJpaRepository.findById(postId) // 영속성 컨텍스트에서 한번 거쳐갈 것 이라서 직접적인 쿼리 X
            .orElseThrow(ErrorCodes.POST_NOT_FOUND::throwException);

        postViewLogJpaRepository.save(PostViewEntity.builder()
            .post(post)
            .userId(userId)
            .build());
    }

    public void savePostLike(Long postId, Long userId) {
        PostEntity post = postJpaRepository.findById(postId)
            .orElseThrow(ErrorCodes.POST_NOT_FOUND::throwException);

        postLikeJpaRepository.save(PostLikeEntity.builder()
            .post(post)
            .userId(userId)
            .build());
    }
}
