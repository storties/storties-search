package org.example.stortiessearch.data.persistence.repository;

import org.example.stortiessearch.data.persistence.model.PostEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostJpaRepository extends CrudRepository<PostEntity, Long> {
}
