package org.example.stortiessearch.infrastructure.cache.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetLikeCountService {

    private final RedisTemplate<String, String> redisTemplate;

    public Map<Long, Long> getLikeCounts(List<Long> postIds) {
        List<String> keys = postIds.stream()
            .map(id -> "post:" + id + ":likes")
            .toList();

        List<String> values = redisTemplate.opsForValue().multiGet(keys);

        Map<Long, Long> result = new HashMap<>();
        for(int i = 0; i < postIds.size(); i++) {
            String value = values.get(i);

            long count = 0L;
            try {
                count = Long.parseLong(value);
            } catch (NumberFormatException ignored) {}
            result.put(postIds.get(i), count);
        }
        return result;
    }
}
