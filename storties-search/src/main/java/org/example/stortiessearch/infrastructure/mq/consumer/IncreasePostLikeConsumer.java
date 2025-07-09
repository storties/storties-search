package org.example.stortiessearch.infrastructure.mq.consumer;

import lombok.RequiredArgsConstructor;
import org.example.stortiessearch.application.event.IncreasePostLikeEvent;
import org.example.stortiessearch.data.persistence.post.CommandPostRepository;
import org.example.stortiessearch.data.persistence.post.QueryPostRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.CONTAINER_FACTORY;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.GROUP_ID;
import static org.example.stortiessearch.infrastructure.mq.KafkaProperties.INCREASE_LIKE_TOPIC;

@Component
@RequiredArgsConstructor
public class IncreasePostLikeConsumer {

    private final RedisTemplate<String, String> redisTemplate;

    private final QueryPostRepository queryPostRepository;

    private final CommandPostRepository commandPostRepository;

    @KafkaListener(
        topics = INCREASE_LIKE_TOPIC,
        groupId = GROUP_ID,
        containerFactory = CONTAINER_FACTORY
    )
    public void consume(IncreasePostLikeEvent event, Acknowledgment ack) {
        String likeCountKey = "post:" + event.getPostId() + ":likes";

        boolean isFirst = (queryPostRepository
            .queryLikeByPostIdAndUserId(event.getPostId(), event.getUserId()) == null);

        if (isFirst) {
            commandPostRepository.savePostLike(event.getPostId(), event.getUserId());
            redisTemplate.opsForValue().increment(likeCountKey);
        }

        ack.acknowledge();
    }
}
