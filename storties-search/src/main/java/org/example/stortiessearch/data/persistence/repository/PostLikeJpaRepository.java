package org.example.stortiessearch.data.persistence.repository;

import org.example.stortiessearch.data.persistence.model.PostLikeEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostLikeJpaRepository extends CrudRepository<PostLikeEntity, Long> {
}
