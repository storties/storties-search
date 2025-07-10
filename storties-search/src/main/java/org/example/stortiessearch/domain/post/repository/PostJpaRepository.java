package org.example.stortiessearch.domain.post.repository;

import org.example.stortiessearch.domain.post.model.PostEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostJpaRepository extends CrudRepository<PostEntity, Long> {
}
