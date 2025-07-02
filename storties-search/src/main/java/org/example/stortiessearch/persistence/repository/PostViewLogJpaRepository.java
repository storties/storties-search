package org.example.stortiessearch.persistence.repository;

import org.example.stortiessearch.persistence.model.PostViewLogEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostViewLogJpaRepository extends CrudRepository<PostViewLogEntity, Long> {
}
