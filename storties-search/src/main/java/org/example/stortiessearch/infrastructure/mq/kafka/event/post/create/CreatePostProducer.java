package org.example.stortiessearch.infrastructure.mq.kafka.event.post.create;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.CreatePostEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.CREATE_TOPIC;

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
