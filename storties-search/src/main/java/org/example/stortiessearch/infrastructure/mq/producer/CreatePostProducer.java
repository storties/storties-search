package org.example.stortiessearch.infrastructure.mq.producer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.CreatePostEvent;
import org.example.stortiessearch.infrastructure.mq.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.CREATE_TOPIC;

@RequiredArgsConstructor
@Component
public class CreatePostProducer {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    private final JsonSerializer jsonSerializer;

    public void publish(CreatePostEvent event) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .topic(CREATE_TOPIC)
            .eventClass(CreatePostEvent.class)
            .payload(jsonSerializer.toJson(event))
            .retryCount(0)
            .build();

        kafkaTemplate.send(CREATE_TOPIC, kafkaEvent);
    }
}
