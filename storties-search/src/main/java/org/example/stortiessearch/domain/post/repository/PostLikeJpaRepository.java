package org.example.stortiessearch.domain.post.repository;

import org.example.stortiessearch.domain.post.model.PostLikeEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostLikeJpaRepository extends CrudRepository<PostLikeEntity, Long> {
}
