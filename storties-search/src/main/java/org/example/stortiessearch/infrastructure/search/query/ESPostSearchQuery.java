package org.example.stortiessearch.infrastructure.search.query;

import co.elastic.clients.elasticsearch._types.FieldValue;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.data.search.post.document.PostDocument;
import org.example.stortiessearch.infrastructure.client.rest.VectorRestClient;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import java.util.List;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ESPostSearchQuery {

    private final ElasticsearchOperations elasticsearchOperations;

    private final VectorRestClient vectorRestClient;

    public List<PostDocument> searchByTitleAndTags(String input) {
        List<String> tokens = List.of(input.trim().split("\\s+"));

        List<FieldValue> tokensFieldValue = tokens.stream()
            .map(FieldValue::of)
            .toList();

        NativeQuery query = NativeQuery.builder()
            .withQuery(q -> q
                .bool(b -> b
                    .must(m -> m
                        .term(t -> t
                            .field("isPublished")
                            .value(true)
                        )
                    )
                    .should(s1 -> s1.fuzzy(f -> f
                        .field("title")
                        .value(input)
                        .fuzziness("AUTO")
                    ))
                    .should(s2 -> s2.terms(t -> t
                            .field("title")
                            .terms(terms -> terms.value(tokensFieldValue))
                        )
                    )
                    .should(s2 -> s2.terms(t -> t
                            .field("content")
                            .terms(terms -> terms.value(tokensFieldValue))
                        )
                    )
                    .should(s3 -> s3
                            .terms(t -> t
                                .field("tags")
                                .terms(terms -> terms.value(tokensFieldValue))
                            )
                    ).minimumShouldMatch("1")
                )
            )
            .withSourceFilter(FetchSourceFilter.of(excludes -> excludes.withExcludes("vector", "isPublished")))
            .build();

        SearchHits<PostDocument> searchHits = elasticsearchOperations
            .search(query, PostDocument.class);

        return searchHits
            .getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }

    public List<PostDocument> searchByKNN(String input) {

        if(input == null|| input.isEmpty()) {
            return List.of();
        }

        float[] vector = vectorRestClient.generateVector(input);

        List<Float> vectorList = new ArrayList<>(vector.length);
        for (float v : vector) {
            vectorList.add(v);
        }

        NativeQuery query = NativeQuery.builder()
            .withQuery(q -> q
                .bool(b -> b
                    .must(m -> m
                        .term(t -> t
                            .field("isPublished")
                            .value(true)
                        )
                    )
                    .must(m -> m
                        .knn(knn -> knn
                        .field("vector")
                        .queryVector(vectorList)
                        .k(10)
                        .numCandidates(100)
                        )
                    )
                )
            )
            .withSourceFilter(FetchSourceFilter.of(excludes -> excludes.withExcludes("vector", "isPublished")))
            .build();

        SearchHits<PostDocument> searchHits = elasticsearchOperations
            .search(query, PostDocument.class);

        return searchHits
            .getSearchHits()
            .stream()
            .map(SearchHit::getContent)
            .collect(Collectors.toList());
    }
}
