package ru.practicum.eventhub.api.mapper;

import org.mapstruct.*;
import ru.practicum.eventhub.api.dto.request.EventCreateDto;
import ru.practicum.eventhub.api.dto.request.EventUpdateDto;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.response.EventDto;
import ru.practicum.eventhub.domain.model.Event;
import ru.practicum.eventhub.domain.model.Tag;

import java.util.Set;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = SPRING)
public interface EventMapper {
    EventDto toDto(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Event fromCreateDto(EventCreateDto createDto);

    @AfterMapping
    default void afterCreate(EventCreateDto createDto, @MappingTarget Event event) {
        addTagsInternal(createDto.tags(), event);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Event updateEventFromDto(EventUpdateDto updateDto, @MappingTarget Event event);

    @AfterMapping
    default void afterUpdate(EventUpdateDto updateDto, @MappingTarget Event event) {
        addTagsInternal(updateDto.tags(), event);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "events", ignore = true)
    Tag tagFromNestedDto(TagCreateDto dto);

    private void addTagsInternal(Set<TagCreateDto> tags, Event event) {
        if (tags == null) {
            return;
        }
        for (TagCreateDto t : tags) {
            Tag tag = tagFromNestedDto(t);
            event.addTag(tag);
        }
    }
}
