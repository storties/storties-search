package org.example.stortiessearch.infrastructure.search.domain.post.repository;

import org.example.stortiessearch.infrastructure.search.domain.post.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {

}
