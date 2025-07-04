package org.example.stortiessearch.support.elastic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.example.stortiessearch.application.event.UpdatePostEvent;
import org.example.stortiessearch.data.search.document.PostDocument;

public class ESPostDirtyChecker {

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
            changes.put("updatedAt", newDoc.getUpdatedAt());
        }
        if (!Objects.equals(oldDoc.getIsPublished(), newDoc.getIsPublished())) {
            changes.put("isPublished", newDoc.getIsPublished());
        }
        // vector, createdAt, userId는 변경하지 않음

        return changes;
    }

}
