package org.example.stortiessearch.infrastructure.mq.kafka.event.post.update;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.UpdatePostEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaTopicProperties.UPDATE_TOPIC;

@RequiredArgsConstructor
@Component
public class UpdatePostProducer {

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    private final JsonSerializer jsonSerializer;


    public void publish(UpdatePostEvent event) {
        KafkaEvent kafkaEvent = KafkaEvent.builder()
            .topic(UPDATE_TOPIC)
            .eventClass(UpdatePostEvent.class)
            .payload(jsonSerializer.toJson(event))
            .retryCount(0)
            .build();

        kafkaTemplate.send(UPDATE_TOPIC, String.valueOf(event.getId()), kafkaEvent);
    }
}
