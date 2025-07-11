package org.example.stortiessearch.infrastructure.mq.kafka.event.like.decrease;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.DecreasePostLikeEvent;
import org.example.stortiessearch.domain.post.CommandPostRepository;
import org.example.stortiessearch.domain.post.QueryPostRepository;
import org.example.stortiessearch.domain.post.model.PostLikeEntity;
import org.example.stortiessearch.infrastructure.mq.kafka.dto.KafkaEvent;
import org.example.stortiessearch.infrastructure.mq.kafka.system.retry.KafkaRetryProducer;
import org.example.stortiessearch.infrastructure.mq.kafka.util.JsonSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaProperties.GROUP_ID;
import static org.example.stortiessearch.infrastructure.mq.kafka.properties.KafkaTopicProperties.DECREASE_LIKE_TOPIC;

@Component
@RequiredArgsConstructor
public class DecreasePostLikeConsumer {

    private final RedisTemplate<String, String> redisTemplate;

    private final CommandPostRepository commandPostRepository;

    private final QueryPostRepository queryPostRepository;

    private final JsonSerializer jsonSerializer;

    private final KafkaRetryProducer kafkaRetryProducer;

    @KafkaListener(
        topics = DECREASE_LIKE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(KafkaEvent kafkaEvent, Acknowledgment ack) {
        try {

            DecreasePostLikeEvent event = jsonSerializer.fromJson(kafkaEvent.getPayload(), DecreasePostLikeEvent.class);

            String likeCountKey = "post:" + event.getPostId() + ":likes";
            PostLikeEntity postLike = queryPostRepository.queryLikeByPostIdAndUserId(event.getPostId(), event.getUserId());
            if (postLike == null) {
                ack.acknowledge();
                return;
            }

            redisTemplate.opsForValue().decrement(likeCountKey);
            commandPostRepository.deleteLikeById(postLike.getId());

            ack.acknowledge();
        } catch (Exception e) {
            kafkaEvent.setErrorMessage(e.getMessage());
            kafkaRetryProducer.retryPublish(kafkaEvent);

            ack.acknowledge();
        }
    }
}
