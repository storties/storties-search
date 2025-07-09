package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostLikeEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.INCREASE_LIKE_TOPIC;

@Component
@RequiredArgsConstructor
public class IncreasePostLikeProducer {

    private final KafkaTemplate<String, IncreasePostLikeEvent> kafkaTemplate;

    public void publish(IncreasePostLikeEvent event) {
        kafkaTemplate.send(INCREASE_LIKE_TOPIC, event);
    }
}
