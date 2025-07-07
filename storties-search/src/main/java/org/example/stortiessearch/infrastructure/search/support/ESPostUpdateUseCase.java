package org.example.stortiessearch.infrastructure.search.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.application.event.UpdatePostEvent;
import org.example.stortiessearch.data.search.document.PostDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ESPostUpdateUseCase {

    private final ElasticsearchOperations elasticsearchOperations;

    public void updateChangedFields(String indexName, String documentId, PostDocument oldPost, UpdatePostEvent newPost) {
        Map<String, Object> diffMap = ESPostDirtyChecker.diff(oldPost, newPost);

        if(diffMap.isEmpty())
            return;

        try {
            // Document 생성 전에 LocalDateTime 변환 확인
            Map<String, Object> safeMap = new HashMap<>();
            for (Map.Entry<String, Object> entry : diffMap.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof LocalDateTime) {
                    // LocalDateTime을 문자열로 변환(ISO)
                    safeMap.put(entry.getKey(), ((LocalDateTime) value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")));
                } else {
                    safeMap.put(entry.getKey(), value);
                }
            }

            Document document = Document.from(safeMap);

            UpdateQuery updateQuery = UpdateQuery.builder(documentId)
                .withDocument(document)
                .build();

            elasticsearchOperations.update(updateQuery, IndexCoordinates.of(indexName));

        } catch (Exception e) {
            log.error("Failed to update document: {}", e.getMessage());
            throw new RuntimeException("Document update failed", e);
        }
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
