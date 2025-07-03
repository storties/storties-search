package org.example.stortiessearch.infrastructure.persistence.repository;

import org.example.stortiessearch.infrastructure.persistence.model.PostEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostJpaRepository extends CrudRepository<PostEntity, Long> {
}
