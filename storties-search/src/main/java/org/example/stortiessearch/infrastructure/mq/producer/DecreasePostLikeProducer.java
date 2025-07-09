package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DecreasePostLikeEvent;
import org.example.stortiessearch.infrastructure.mq.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DecreasePostLikeProducer {

    private final KafkaTemplate<String, DecreasePostLikeEvent> kafkaTemplate;

    public void publish(DecreasePostLikeEvent event) {
        kafkaTemplate.send(KafkaProperties.DECREASE_LIKE_TOPIC, event);
    }
}
