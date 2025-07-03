package org.example.stortiessearch.infrastructure.persistence.repository;

import org.example.stortiessearch.infrastructure.persistence.model.PostLikeEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostLikeJpaRepository extends CrudRepository<PostLikeEntity, Long> {
}
