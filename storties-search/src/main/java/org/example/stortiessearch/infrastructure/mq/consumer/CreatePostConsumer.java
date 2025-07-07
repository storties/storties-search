package org.example.stortiessearch.infrastructure.mq.consumer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.CreatePostEvent;
import org.example.stortiessearch.data.search.document.PostDocument;
import org.example.stortiessearch.data.search.repository.PostSearchRepository;
import org.example.stortiessearch.infrastructure.client.rest.VectorRestClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.CREATE_TOPIC;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.GROUP_ID;

// todo PostgreSQL랑 원자성 보장
@Component
@RequiredArgsConstructor
public class CreatePostConsumer {

    private final PostSearchRepository postSearchRepository;

    private final VectorRestClient vectorRestClient;

    @KafkaListener(
        topics = CREATE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(CreatePostEvent event, Acknowledgment ack) {
        try {
            float[] vector = vectorRestClient.generateVector(String.valueOf(event.getId()), event.getTitle(), event.getContent());

            postSearchRepository.save(PostDocument.builder()
                .id(event.getId())
                .title(event.getTitle())
                .content(event.getContent())
                .userId(event.getUserId())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .tags(event.getTags())
                .isPublished(event.getIsPublished())
                .vector(vector)
                .build());

            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
