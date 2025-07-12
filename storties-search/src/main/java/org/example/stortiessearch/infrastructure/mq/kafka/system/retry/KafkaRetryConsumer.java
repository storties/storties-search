package org.example.stortiessearch.infrastructure.mq.kafka.system.retry;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.infrastructure.mq.kafka.system.deadletter.DeadLetterEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.system.deadletter.DeadLetterProducer;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.GROUP_ID;
import static org.example.stortiessearch.infrastructure.mq.kafka.KafkaProperties.RETRY_TOPIC;

@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaRetryConsumer {

    private final JsonSerializer jsonSerializer;

    private final DeadLetterProducer deadLetterProducer;

    private final RedisTemplate<String, String> redisTemplate;

    @KafkaListener(
        topics = RETRY_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(KafkaEvent kafkaEvent, Acknowledgment ack) {
        String redisKey;
        Duration delay;

        kafkaEvent.increaseRetryCount();
        log.warn("{} failed. (retryCount: {}) \n errorMessage: {}", kafkaEvent.getTopic(),
            kafkaEvent.getRetryCount(), kafkaEvent.getErrorMessage());

        int retryCount = kafkaEvent.getRetryCount();
        String topic = kafkaEvent.getTopic();

        if (retryCount <= 2) {
            redisKey = "redis:short:" + topic;
            delay = Duration.ofSeconds(30);
        } else if (retryCount <= 5){
            redisKey = "redis:long:" + topic;
            delay = Duration.ofSeconds(120);
        } else {
            deadLetterProducer.publishDeadLetter(DeadLetterEvent.builder()
                .OriginalTopic(topic)
                .failedAt(LocalDateTime.now())
                .originalPayload(kafkaEvent.getPayload())
                .lastErrorMessage(kafkaEvent.getErrorMessage())
                .build());

            ack.acknowledge();
            return;
        }

        try {
            String json = jsonSerializer.toJson(kafkaEvent);
            long score = System.currentTimeMillis() + delay.toMillis();
            redisTemplate.opsForZSet().add(redisKey, json, score);
        } catch (Exception e) {
            log.error("Failed to serialize CreatePostEvent for retry", e);
        }
        ack.acknowledge();
    }
}
