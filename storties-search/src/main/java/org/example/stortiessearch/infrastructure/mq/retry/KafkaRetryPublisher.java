package org.example.stortiessearch.infrastructure.mq.retry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.stortiessearch.infrastructure.mq.dto.KafkaEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaRetryPublisher {

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    public void retryPublish(KafkaEvent kafkaEvent) {

        String redisKey;
        Duration delay;

        kafkaEvent.increaseRetryCount();
        log.warn("{} failed. (retryCount: {}) ", kafkaEvent.getTopic(), kafkaEvent.getRetryCount());

        int retryCount = kafkaEvent.getRetryCount();
        String topic = kafkaEvent.getTopic();

        if (retryCount <= 2) {
            redisKey = "redis:short:" + topic;
            delay = Duration.ofSeconds(30);
        } else if (retryCount <= 5){
            redisKey = "redis:long:" + topic;
            delay = Duration.ofSeconds(120);
        } else {
            // todo DLQ로 보내는 로직
            return;
        }

        try {
            String json = objectMapper.writeValueAsString(kafkaEvent);
            long score = System.currentTimeMillis() + delay.toMillis();
            redisTemplate.opsForZSet().add(redisKey, json, score);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize CreatePostEvent for retry", e);
        }
    }
}
