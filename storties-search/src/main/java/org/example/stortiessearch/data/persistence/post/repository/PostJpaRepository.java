package org.example.stortiessearch.data.persistence.post.repository;

import org.example.stortiessearch.data.persistence.post.model.PostEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostJpaRepository extends CrudRepository<PostEntity, Long> {
}
