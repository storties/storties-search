package org.example.stortiessearch.persistence.repository;

import org.example.stortiessearch.persistence.model.PostLikeEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostLikeJpaRepository extends CrudRepository<PostLikeEntity, Long> {
}
