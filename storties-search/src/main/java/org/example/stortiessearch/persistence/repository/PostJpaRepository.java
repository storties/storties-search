package org.example.stortiessearch.persistence.repository;

import org.example.stortiessearch.persistence.model.PostEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostJpaRepository extends CrudRepository<PostEntity, Long> {
}
