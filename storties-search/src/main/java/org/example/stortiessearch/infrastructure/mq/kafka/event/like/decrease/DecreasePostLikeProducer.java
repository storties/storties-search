package org.example.stortiessearch.infrastructure.mq.kafka.event.like.decrease;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DecreasePostLikeEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.DECREASE_LIKE_TOPIC;

@Component
@RequiredArgsConstructor
public class DecreasePostLikeProducer {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    private final JsonSerializer jsonSerializer;

    public void publish(DecreasePostLikeEvent event) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .topic(DECREASE_LIKE_TOPIC)
            .eventClass(DecreasePostLikeEvent.class)
            .payload(jsonSerializer.toJson(event))
            .retryCount(0)
            .build();

        kafkaTemplate.send(DECREASE_LIKE_TOPIC, kafkaEvent);
    }
}
