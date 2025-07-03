package org.example.stortiessearch.persistence.repository;

import org.example.stortiessearch.persistence.model.PostViewEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostViewLogJpaRepository extends CrudRepository<PostViewEntity, Long> {
}
