package org.example.stortiessearch.data.search.repository;

import org.example.stortiessearch.data.search.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {

}
