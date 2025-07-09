package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostViewEvent;
import org.example.stortiessearch.infrastructure.mq.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IncreasePostViewProducer {

    private final KafkaTemplate<String, IncreasePostViewEvent> kafkaTemplate;

    public void publish(IncreasePostViewEvent event) {
        kafkaTemplate.send(KafkaProperties.INCREASE_VIEW_TOPIC, event);
    }
}
