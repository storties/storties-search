package org.example.stortiessearch.application.service.dto.request;

import java.util.List;

public record UpdatePostRequest (
    String title,
    String content,
    List<String> tags,
    Boolean isPublished
) {

}
