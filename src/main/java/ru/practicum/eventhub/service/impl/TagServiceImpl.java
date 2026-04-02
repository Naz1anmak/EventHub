package ru.practicum.eventhub.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.api.dto.response.TagStatsDto;
import ru.practicum.eventhub.api.dto.response.TagWithStatsDto;
import ru.practicum.eventhub.api.exception.ConflictException;
import ru.practicum.eventhub.api.mapper.TagMapper;
import ru.practicum.eventhub.api.model.PageOfTags;
import ru.practicum.eventhub.config.feign.TagAnalyticsFacade;
import ru.practicum.eventhub.config.redis.ManyToManyCacheIndexService;
import ru.practicum.eventhub.model.ActionType;
import ru.practicum.eventhub.model.Event;
import ru.practicum.eventhub.model.Tag;
import ru.practicum.eventhub.repository.TagRepository;
import ru.practicum.eventhub.service.TagService;
import ru.practicum.eventhub.service.cache.EventTagCacheService;
import ru.practicum.eventhub.util.PageValidator;
import ru.practicum.eventhub.validation.TagValidationService;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private static final String EVENT_TAG_RELATION = "event_tag";

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final TagReadService tagReadService;
    private final EventReadService eventReadService;
    private final TagValidationService tagValidationService;
    private final TagAnalyticsFacade tagAnalyticsFacade;
    private final EventTagCacheService eventTagCacheService;
    private final ManyToManyCacheIndexService relationIndexService;
    private final TransactionTemplate transactionTemplate;
    private final CompensationService compensationService;

    @Override
    @Transactional
    public TagWithStatsDto addTagToEvent(UUID eventId, UUID tagId) {
        boolean tagAdded = false;
        boolean incrementUsage = false;

        try {
            Tag tag = addTagLocal(eventId, tagId);
            tagAdded = true;

            TagStatsDto tagStatsDto = tagAnalyticsFacade.createTagAnalytics(tagId);
            incrementUsage = true;

            cacheRefreshAfterAddTag(eventId, tagId);

            log.info("Тег с id={} добавлен к событию с id={}", tagId, eventId);
            return tagMapper.toDtoWithStats(tag, tagStatsDto);

        } catch (Exception ex) {
            log.error("Ошибка при добавлении тега с id={} к событию с id={}: {}", tagId, eventId, ex.getMessage());

            if (incrementUsage) {
                try {
                    tagAnalyticsFacade.deleteTagAnalytics(tagId);
                } catch (Exception exception) {
                    log.error("Не удалось откатить аналитику сразу, сохраняем в Scheduler");
                    compensationService.saveForLater(tagId, ActionType.DELETE_ANALYTICS);
                }
            }

            if (tagAdded) {
                removeTagLocal(eventId, tagId);
            }

            throw ex;
        }
    }

    @Override
    @Transactional
    public TagDto createTag(TagCreateDto dto) {
        tagValidationService.validateCreate(dto);
        Tag tag = tagMapper.fromCreateDto(dto);

        tag = tagRepository.save(tag);
        log.info("Создан тег с id={}", tag.getId());
        return tagMapper.toDto(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public PageOfTags getTags(Pageable pageable) {
        Page<Tag> page = tagRepository.findAll(pageable);
        PageValidator.validatePage(page);

        log.info("Запрошены теги: страница {}, размер {}", pageable.getPageNumber(), pageable.getPageSize());
        return toApiPage(page, tagMapper);
    }

    @Override
    @Transactional(readOnly = true)
    public PageOfTags getTagsByEvent(UUID eventId, Pageable pageable) {
        eventReadService.findById(eventId);
        Page<Tag> page = tagRepository.findAllByEventsId(eventId, pageable);
        PageValidator.validatePage(page);

        log.info("Запрошены теги для события с id={}: страница {}, размер {}",
                eventId, pageable.getPageNumber(), pageable.getPageSize());
        return toApiPage(page, tagMapper);
    }

    @Override
    @Cacheable(value = "tags_by_event", key = "#eventId + ':' + #tagId")
    public TagWithStatsDto getTagByEvent(UUID eventId, UUID tagId) {
        this.transactionTemplate.setReadOnly(true);
        Tag tag = transactionTemplate.execute(status -> {
            eventReadService.findById(eventId);
            return tagReadService.findByIdAndEventsId(tagId, eventId);
        });

        TagStatsDto stats = tagAnalyticsFacade.getTagStats(tagId);

        log.info("Запрошен тег с id={} для события с id={}", tagId, eventId);
        return tagMapper.toDtoWithStats(tag, stats);
    }

    @Override
    @CachePut(value = "tags", key = "#tagId")
    public TagDto updateTag(UUID tagId, TagUpdateDto dto) {
        Tag tag = transactionTemplate.execute(status -> {
            Tag tagEntity = tagReadService.findById(tagId);

            tagValidationService.validateUpdate(dto, tagEntity);

            tagEntity = tagMapper.updateTagFromDto(dto, tagEntity);

            tagEntity = tagRepository.save(tagEntity);
            return tagEntity;
        });

        Set<UUID> eventIds = relationIndexService.getLeftIds(EVENT_TAG_RELATION, tagId);
        eventTagCacheService.refreshCompositeCacheForTag(tag, eventIds);

        log.info("Обновлен тег с id={}", tagId);
        return tagMapper.toDto(tag);
    }

    @Override
    public void deleteForEvent(UUID eventId, UUID tagId) {
        transactionTemplate.executeWithoutResult(status -> {
            Event event = eventReadService.findById(eventId);
            Tag tag = tagReadService.findByIdAndEventsId(tagId, eventId);

            event.removeTag(tag);
        });

        relationIndexService.remove(EVENT_TAG_RELATION, eventId, tagId);

        eventReadService.findDtoById(eventId);
        eventTagCacheService.refreshByEventBatch(eventId, Set.of(tagId));

        log.info("Тег с id={} отвязан от события с id={}", tagId, eventId);
    }

    @Override
    @CacheEvict(value = "tags", key = "#tagId")
    public void deleteTag(UUID tagId) {
        transactionTemplate.executeWithoutResult(status -> {
            Tag tag = tagReadService.findById(tagId);

            Iterator<Event> iterator = tag.getEvents().iterator();
            while (iterator.hasNext()) {
                Event event = iterator.next();
                iterator.remove();
                event.getTags().remove(tag);
            }

            tagRepository.deleteById(tagId);
        });

        eventTagCacheService.evictEventTagCache(tagId);

        log.info("Удален тег с id={}", tagId);
    }

    @Override
    public void cacheRefreshAfterAddTag(UUID eventId, UUID tagId) {
        eventReadService.findDtoById(eventId);
        tagReadService.findDtoById(tagId);
        log.info("Кэш обновлен для события с id={} и тега с id={}", eventId, tagId);
    }

    @Transactional
    public Tag addTagLocal(UUID eventId, UUID tagId) {
        Event event = eventReadService.findById(eventId);
        Tag tag = tagReadService.findById(tagId);

        if (tagReadService.existsByIdAndEventsId(tagId, eventId)) {
            log.error("Тег с id={} уже добавлен к событию с id={}", tagId, eventId);
            throw new ConflictException("Тег с id=" + tagId + " уже добавлен к событию с id=" + eventId);
        }

        event.addTag(tag);
        relationIndexService.add(EVENT_TAG_RELATION, eventId, tagId);

        return tag;
    }

    @Transactional
    public void removeTagLocal(UUID eventId, UUID tagId) {
        Event event = eventReadService.findById(eventId);
        Tag tag = tagReadService.findById(tagId);

        event.removeTag(tag);

        relationIndexService.remove(EVENT_TAG_RELATION, eventId, tagId);

        log.info("Тег с id={} удален из события с id={}", tagId, eventId);
    }

    private PageOfTags toApiPage(Page<Tag> page, TagMapper mapper) {
        PageOfTags apiPage = new PageOfTags();
        apiPage.setContent(page.getContent().stream().map(mapper::toDto).toList());
        apiPage.setPageNumber(page.getNumber());
        apiPage.setPageSize(page.getSize());
        apiPage.setTotalElements(page.getTotalElements());
        apiPage.setTotalPages(page.getTotalPages());
        return apiPage;
    }
}
