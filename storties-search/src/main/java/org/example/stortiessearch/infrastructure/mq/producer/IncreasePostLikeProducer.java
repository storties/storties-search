package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostLikeEvent;
import org.example.stortiessearch.infrastructure.mq.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.INCREASE_LIKE_TOPIC;

@Component
@RequiredArgsConstructor
public class IncreasePostLikeProducer {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    private final JsonSerializer jsonSerializer;

    public void publish(IncreasePostLikeEvent event) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .topic(INCREASE_LIKE_TOPIC)
            .eventClass(IncreasePostLikeEvent.class)
            .payload(jsonSerializer.toJson(event))
            .retryCount(0)
            .build();

        kafkaTemplate.send(INCREASE_LIKE_TOPIC, kafkaEvent);
    }
}
