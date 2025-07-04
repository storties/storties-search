package org.example.stortiessearch.application.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.UpdatePostEvent;
import org.example.stortiessearch.application.service.dto.request.UpdatePostRequest;
import org.example.stortiessearch.global.exception.error.ErrorCodes;
import org.example.stortiessearch.data.persistence.model.PostEntity;
import org.example.stortiessearch.data.persistence.repository.PostJpaRepository;
import org.example.stortiessearch.infrastructure.mq.producer.UpdatePostProducer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePostService {

    private final PostJpaRepository postJpaRepository;

    private final UpdatePostProducer updatePostProducer;

    public void execute(Long postId, UpdatePostRequest request) {

        PostEntity postEntity = postJpaRepository.findById(postId)
            .orElseThrow(ErrorCodes.POST_NOT_FOUND::throwException);

        postEntity = postJpaRepository.save(postEntity.toBuilder()
            .title(request.title())
            .content(request.content())
            .tags(request.tags())
            .isPublished(request.isPublished())
            .updatedAt(LocalDateTime.now())
            .build());

        updatePostProducer.publish(UpdatePostEvent.from(postEntity));
    }
}
