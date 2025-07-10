package org.example.stortiessearch.application.usecase;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DeletePostEvent;
import org.example.stortiessearch.common.annotations.UseCase;
import org.example.stortiessearch.infrastructure.mq.producer.DeletePostProducer;
import org.example.stortiessearch.global.authentication.AuthenticatedUserProvider;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.infrastructure.client.grpc.user.dto.AuthenticatedUser;
import org.example.stortiessearch.domain.post.CommandPostRepository;
import org.example.stortiessearch.domain.post.QueryPostRepository;
import org.example.stortiessearch.domain.post.model.PostEntity;

@UseCase
@RequiredArgsConstructor
public class DeletePostUseCase {

    private final CommandPostRepository commandPostRepository;

    private final QueryPostRepository queryPostRepository;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    private final DeletePostProducer deletePostProducer;

    public void execute(Long postId) {
        PostEntity postEntity = queryPostRepository.queryPostById(postId);
        AuthenticatedUser authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();

        if(Objects.equals(authenticatedUser.userId(), postEntity.getId())) {
           throw ErrorCodes.POST_DELETE_FORBIDDEN.throwException();
        }

        commandPostRepository.deletePostByPostId(postId);

        deletePostProducer.publish(new DeletePostEvent(postEntity.getId()));
    }
}
