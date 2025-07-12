package org.example.stortiessearch.infrastructure.mq.kafka.system.deadletter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaProperties.GROUP_ID;
import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaTopicProperties.DEAD_LETTER_TOPIC;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeadLetterConsumer {

    private final JsonSerializer jsonSerializer;

    @KafkaListener(
        topics = DEAD_LETTER_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(KafkaEvent kafkaEvent, Acknowledgment ack) {
        try {
            String payload = kafkaEvent.getPayload();
            DeadLetterEvent event = (DeadLetterEvent) jsonSerializer.fromJson(payload, kafkaEvent.getEventClass());

            log.error("Processing DLQ message: topic={}, \n error={}",
                event.getOriginalTopic(),
                event.getLastErrorMessage());

            // todo DB 저장 및 알림 발송 기능

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process DLQ message: {}", e.getMessage());
            ack.acknowledge();
        }
    }
}
