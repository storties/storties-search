package org.example.stortiessearch.infrastructure.mq.kafka.event.viewlog;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostViewEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.INCREASE_VIEW_TOPIC;

@RequiredArgsConstructor
@Component
public class IncreasePostViewProducer {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    private final JsonSerializer jsonSerializer;

    public void publish(IncreasePostViewEvent event) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .topic(INCREASE_VIEW_TOPIC)
            .eventClass(IncreasePostViewEvent.class)
            .payload(jsonSerializer.toJson(event))
            .retryCount(0)
            .build();

        kafkaTemplate.send(INCREASE_VIEW_TOPIC, kafkaEvent);
    }
}
