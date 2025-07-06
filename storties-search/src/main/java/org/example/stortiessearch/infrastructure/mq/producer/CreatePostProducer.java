package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.CreatePostEvent;
import org.example.stortiessearch.infrastructure.mq.KafkaProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CreatePostProducer {

    private final KafkaTemplate<String, CreatePostEvent> kafkaTemplate;


    public void publish(CreatePostEvent event) {
        kafkaTemplate.send(KafkaProperties.CREATE_TOPIC, String.valueOf(event.getId()), event);
    }
}
