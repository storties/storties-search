package org.example.stortiessearch.infrastructure.mq.consumer;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.application.event.DeletePostEvent;
import org.example.stortiessearch.data.search.document.PostDocument;
import org.example.stortiessearch.data.search.repository.PostSearchRepository;
import org.example.stortiessearch.infrastructure.mq.KafkaProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.DELETE_TOPIC;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.GROUP_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeletePostConsumer {

    private final PostSearchRepository postSearchRepository;

    @KafkaListener(
        topics = DELETE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(DeletePostEvent event, Acknowledgment ack) {
        try {
            Optional<PostDocument> post = postSearchRepository.findById(event.getPostId());
            if (post.isEmpty()) {
                ack.acknowledge();
                return;
            }

            postSearchRepository.deleteById(event.getPostId());

            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
