package ru.practicum.eventhub.infrastructure.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManyToManyCacheIndexService {
    private final RedisTemplate<String, String> redisTemplate;

    private String leftKey(String relation, UUID leftId) {
        return relation + ":left:" + leftId;
    }

    private String rightKey(String relation, UUID rightId) {
        return relation + ":right:" + rightId;
    }

    public void add(String relation, UUID leftId, UUID rightId) {
        redisTemplate.opsForSet().add(leftKey(relation, leftId), rightId.toString());
        redisTemplate.opsForSet().add(rightKey(relation, rightId), leftId.toString());
    }

    public void remove(String relation, UUID leftId, UUID rightId) {
        redisTemplate.opsForSet().remove(leftKey(relation, leftId), rightId.toString());
        redisTemplate.opsForSet().remove(rightKey(relation, rightId), leftId.toString());
    }

    public Set<UUID> getRightIds(String relation, UUID leftId) {
        Set<String> ids = redisTemplate.opsForSet().members(leftKey(relation, leftId));
        if (ids == null) return Collections.emptySet();
        return ids.stream().map(UUID::fromString).collect(Collectors.toSet());
    }

    public Set<UUID> getLeftIds(String relation, UUID rightId) {
        Set<String> ids = redisTemplate.opsForSet().members(rightKey(relation, rightId));
        if (ids == null) return Collections.emptySet();
        return ids.stream().map(UUID::fromString).collect(Collectors.toSet());
    }

    public void deleteLeft(String relation, UUID leftId) {
        redisTemplate.delete(leftKey(relation, leftId));
    }

    public void deleteRight(String relation, UUID rightId) {
        redisTemplate.delete(rightKey(relation, rightId));
    }
}
