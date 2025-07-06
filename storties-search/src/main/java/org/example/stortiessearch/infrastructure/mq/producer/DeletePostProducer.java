package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DeletePostEvent;
import org.example.stortiessearch.infrastructure.mq.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DeletePostProducer {

    private final KafkaTemplate<String, DeletePostEvent> kafkaTemplate;

    public void publish(DeletePostEvent event) {
        kafkaTemplate.send(KafkaProperties.DELETE_TOPIC, event);
    }
}
