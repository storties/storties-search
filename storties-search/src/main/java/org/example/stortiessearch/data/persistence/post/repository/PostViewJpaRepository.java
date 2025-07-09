package org.example.stortiessearch.data.persistence.post.repository;

import org.example.stortiessearch.data.persistence.post.model.PostViewEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostViewJpaRepository extends CrudRepository<PostViewEntity, Long> {
}
