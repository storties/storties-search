package org.example.stortiessearch.domain.post.repository;

import org.example.stortiessearch.domain.post.model.PostViewEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostViewJpaRepository extends CrudRepository<PostViewEntity, Long> {
}
