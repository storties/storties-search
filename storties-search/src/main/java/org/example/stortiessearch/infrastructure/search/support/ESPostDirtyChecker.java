package org.example.stortiessearch.infrastructure.search.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.example.stortiessearch.application.event.UpdatePostEvent;
import org.example.stortiessearch.infrastructure.search.domain.post.document.PostDocument;

public class ESPostDirtyChecker {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");


    public static Map<String, Object> diff(PostDocument oldDoc, UpdatePostEvent newDoc) {
        Map<String, Object> changes = new HashMap<>();

        if (!Objects.equals(oldDoc.getTitle(), newDoc.getTitle())) {
            changes.put("title", newDoc.getTitle());
        }
        if (!Objects.equals(oldDoc.getContent(), newDoc.getContent())) {
            changes.put("content", newDoc.getContent());
        }
        if (!Objects.equals(oldDoc.getTags(), newDoc.getTags())) {
            changes.put("tags", newDoc.getTags());
        }
        if (!Objects.equals(oldDoc.getUpdatedAt(), newDoc.getUpdatedAt())) {
            LocalDateTime updatedAt = newDoc.getUpdatedAt();
            if (updatedAt != null) {
                changes.put("updatedAt", updatedAt.format(FORMATTER));
            }
        }
        if (!Objects.equals(oldDoc.getIsPublished(), newDoc.getIsPublished())) {
            changes.put("isPublished", newDoc.getIsPublished());
        }
        // vector, createdAt, userId는 변경하지 않음

        return changes;
    }

}
