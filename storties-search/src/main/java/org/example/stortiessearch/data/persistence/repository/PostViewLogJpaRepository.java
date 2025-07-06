package org.example.stortiessearch.data.persistence.repository;

import org.example.stortiessearch.data.persistence.model.PostViewEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostViewLogJpaRepository extends CrudRepository<PostViewEntity, Long> {
}
