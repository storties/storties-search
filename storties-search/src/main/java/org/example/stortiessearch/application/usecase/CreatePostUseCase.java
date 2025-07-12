package org.example.stortiessearch.application.usecase;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.CreatePostEvent;
import org.example.stortiessearch.common.annotations.UseCase;
import org.example.stortiessearch.infrastructure.mq.kafka.event.post.create.CreatePostProducer;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.example.stortiessearch.domain.post.CommandPostRepository;
import org.example.stortiessearch.domain.post.model.PostEntity;
import org.example.stortiessearch.application.usecase.dto.request.CreatePostRequest;

@UseCase
@RequiredArgsConstructor
public class CreatePostUseCase {

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
