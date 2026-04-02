package ru.practicum.eventhub.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectCacheIndexService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String CATEGORY_PROJECTS_PREFIX = "category_projects:";

    private String buildKey(UUID categoryId) {
        return CATEGORY_PROJECTS_PREFIX + categoryId;
    }

    public void addProject(UUID categoryId, UUID projectId) {
        redisTemplate.opsForSet()
                .add(buildKey(categoryId), projectId.toString());
    }

    public void removeProject(UUID categoryId, UUID projectId) {
        redisTemplate.opsForSet()
                .remove(buildKey(categoryId), projectId.toString());
    }

    public Set<String> getProjects(UUID categoryId) {
        return redisTemplate.opsForSet()
                .members(buildKey(categoryId));
    }

    public void deleteCategoryIndex(UUID categoryId) {
        redisTemplate.delete(buildKey(categoryId));
    }
}
