package org.example.stortiessearch.infrastructure.cache.service;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetViewCountService {

    private final RedisTemplate<String, String> redisTemplate;

    public Map<Long, Long> getViewCounts(List<Long> postIds) {
        List<String> keys = postIds.stream()
            .map(id -> "post:" + id + ":views")
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
