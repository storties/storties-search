package org.example.stortiessearch.infrastructure.mq.kafka.event.post.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.application.event.CreatePostEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.system.retry.KafkaRetryProducer;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.example.stortiessearch.infrastructure.search.domain.post.document.PostDocument;
import org.example.stortiessearch.infrastructure.search.domain.post.repository.PostSearchRepository;
import org.example.stortiessearch.infrastructure.client.rest.VectorRestClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.CREATE_TOPIC;
import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.GROUP_ID;

// todo outbox 적용
@Component
@RequiredArgsConstructor
@Slf4j
public class CreatePostConsumer {

    private final PostSearchRepository postSearchRepository;

    private final VectorRestClient vectorRestClient;

    private final JsonSerializer jsonSerializer;

    private final KafkaRetryProducer kafkaRetryProducer;

    @KafkaListener(
        topics = CREATE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(KafkaEvent kafkaEvent, Acknowledgment ack) {
        try {
            CreatePostEvent event = (CreatePostEvent) jsonSerializer.fromJson(kafkaEvent.getPayload(), kafkaEvent.getEventClass());

            float[] vector = vectorRestClient.generateVector(event.getTitle() + event.getContent());

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
            kafkaEvent.setErrorMessage(e.getMessage());
            kafkaRetryProducer.retryPublish(kafkaEvent);

            ack.acknowledge();
        }
    }
}
