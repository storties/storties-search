package org.example.stortiessearch.support.elastic;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.UpdatePostEvent;
import org.example.stortiessearch.data.search.document.PostDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ESPostUpdateUseCase {

    private final ElasticsearchOperations elasticsearchOperations;

    public void updateChangedFields(String indexName, String documentId, PostDocument oldPost, UpdatePostEvent newPost) {
        Map<String, Object> diffMap = ESPostDirtyChecker.diff(oldPost, newPost);

        if(diffMap.isEmpty())
            return;

        Document document = Document.from(diffMap);

        UpdateQuery updateQuery = UpdateQuery.builder(documentId)
            .withDocument(document)
            .build();

        elasticsearchOperations.update(updateQuery, IndexCoordinates.of(indexName));
    }

    public void updateVector(String indexName, String documentId, float[] vector) {
        Map<String, Object> vectorMap = new HashMap<>();
        vectorMap.put("vector", vector);
        Document document = Document.from(vectorMap);

        UpdateQuery updateQuery = UpdateQuery.builder(documentId)
            .withDocument(document)
            .build();

        elasticsearchOperations.update(updateQuery, IndexCoordinates.of(indexName));
    }
}
