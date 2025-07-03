package org.example.stortiessearch.infrastructure.search.repository;

import org.example.stortiessearch.infrastructure.search.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {

}
