package org.example.stortiessearch.application.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.common.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.grpc.user.AuthenticatedUser;
import org.example.stortiessearch.infrastructure.persistence.CommandPostRepository;
import org.example.stortiessearch.infrastructure.persistence.model.PostEntity;
import org.example.stortiessearch.application.service.dto.request.CreatePostRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostService {

    private final CommandPostRepository commandPostRepository;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public void execute(CreatePostRequest request) {

        AuthenticatedUser user = authenticatedUserProvider.getAuthenticatedUser();

        commandPostRepository.savePost(PostEntity
            .builder()
            .userId(user.userId())
            .tags(request.tags())
            .title(request.title())
            .content(request.content())
            .isPublished(request.isPublished())
            .updatedAt(LocalDateTime.now())
            .build());
    }
}
