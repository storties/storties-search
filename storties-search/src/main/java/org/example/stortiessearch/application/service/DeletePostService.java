package org.example.stortiessearch.application.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.common.AuthenticatedUserProvider;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.infrastructure.grpc.user.AuthenticatedUser;
import org.example.stortiessearch.infrastructure.persistence.CommandPostRepository;
import org.example.stortiessearch.infrastructure.persistence.QueryPostRepository;
import org.example.stortiessearch.infrastructure.persistence.model.PostEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePostService {

    private final CommandPostRepository commandPostRepository;

    private final QueryPostRepository queryPostRepository;

    private final AuthenticatedUserProvider authenticatedUserProvider;

    public void execute(Long postId) {
        PostEntity postEntity = queryPostRepository.queryPostById(postId);
        AuthenticatedUser authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();

        if(!Objects.equals(authenticatedUser.userId(), postEntity.getId())) {
           throw ErrorCodes.POST_DELETE_FORBIDDEN.throwException();
        }

        commandPostRepository.deletePostByPostId(postId);
    }
}
