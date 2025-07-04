package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.UpdatePostEvent;
import org.example.stortiessearch.infrastructure.mq.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UpdatePostProducer {

    private final KafkaTemplate<String, UpdatePostEvent> kafkaTemplate;


    public void publish(UpdatePostEvent event) {
        kafkaTemplate.send(KafkaProperties.UPDATE_TOPIC, String.valueOf(event.getId()), event);
    }
}
