package org.example.stortiessearch.infrastructure.mq.kafka.event.post.delete;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.application.event.DeletePostEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.system.retry.KafkaRetryProducer;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.example.stortiessearch.infrastructure.search.domain.post.document.PostDocument;
import org.example.stortiessearch.infrastructure.search.domain.post.repository.PostSearchRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.DELETE_TOPIC;
import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.GROUP_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeletePostConsumer {

    private final PostSearchRepository postSearchRepository;

    private final JsonSerializer jsonSerializer;

    private final KafkaRetryProducer kafkaRetryProducer;

    @KafkaListener(
        topics = DELETE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(KafkaEvent kafkaEvent, Acknowledgment ack) {
        String payload = kafkaEvent.getPayload();
        DeletePostEvent event = jsonSerializer.fromJson(payload, DeletePostEvent.class);

        try {
            Optional<PostDocument> post = postSearchRepository.findById(event.getPostId());
            if (post.isEmpty()) {
                ack.acknowledge();
                return;
            }

            postSearchRepository.deleteById(event.getPostId());

            ack.acknowledge();
        } catch (Exception e) {
            kafkaEvent.setErrorMessage(e.getMessage());
            kafkaRetryProducer.retryPublish(kafkaEvent);

            ack.acknowledge();
        }
    }
}
