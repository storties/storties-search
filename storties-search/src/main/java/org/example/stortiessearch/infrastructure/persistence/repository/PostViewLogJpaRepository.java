package org.example.stortiessearch.infrastructure.persistence.repository;

import org.example.stortiessearch.infrastructure.persistence.model.PostViewEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostViewLogJpaRepository extends CrudRepository<PostViewEntity, Long> {
}
