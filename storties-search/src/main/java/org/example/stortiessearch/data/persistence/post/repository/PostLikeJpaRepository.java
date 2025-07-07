package org.example.stortiessearch.data.persistence.post.repository;

import org.example.stortiessearch.data.persistence.post.model.PostLikeEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostLikeJpaRepository extends CrudRepository<PostLikeEntity, Long> {
}
