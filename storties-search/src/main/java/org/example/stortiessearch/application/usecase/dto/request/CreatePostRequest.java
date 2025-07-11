package org.example.stortiessearch.application.usecase.dto.request;

import java.util.List;

public record CreatePostRequest (
    String title,
    String content,
    List<String> tags,
    Boolean isPublished
){

}
