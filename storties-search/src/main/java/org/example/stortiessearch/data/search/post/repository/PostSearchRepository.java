package org.example.stortiessearch.data.search.post.repository;

import org.example.stortiessearch.data.search.post.document.PostDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostSearchRepository extends ElasticsearchRepository<PostDocument, Long> {

}
