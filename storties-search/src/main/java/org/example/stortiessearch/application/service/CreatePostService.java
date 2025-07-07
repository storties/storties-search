package org.example.stortiessearch.application.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.CreatePostEvent;
import org.example.stortiessearch.infrastructure.mq.producer.CreatePostProducer;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.example.stortiessearch.data.persistence.post.CommandPostRepository;
import org.example.stortiessearch.data.persistence.post.model.PostEntity;
import org.example.stortiessearch.application.service.dto.request.CreatePostRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostService {

    private final CommandPostRepository commandPostRepository;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    private final CreatePostProducer createPostProducer;

    public void execute(CreatePostRequest request) {

        AuthenticatedUser user = authenticatedUserProvider.getAuthenticatedUser();

        PostEntity postEntity = commandPostRepository.savePost(PostEntity
            .builder()
            .userId(user.userId())
            .tags(request.tags())
            .title(request.title())
            .content(request.content())
            .isPublished(request.isPublished())
            .updatedAt(LocalDateTime.now())
            .build());

        createPostProducer.publish(CreatePostEvent.from(postEntity));
    }
}
