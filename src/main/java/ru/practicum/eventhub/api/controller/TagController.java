package ru.practicum.eventhub.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.eventhub.api.dto.request.TagCreateDto;
import ru.practicum.eventhub.api.dto.request.TagUpdateDto;
import ru.practicum.eventhub.api.dto.response.TagDto;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.TagService;

import java.util.UUID;

@RestController
@RequestMapping("/tags")
@Validated
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDto createTag(@Valid @RequestBody TagCreateDto dto) {
        return tagService.createTag(dto);
    }

    @GetMapping
    public PagedResponse<TagDto> getTags(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive int size
    ) {
        return tagService.getTags(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public TagDto getTagById(@PathVariable UUID id) {
        return tagService.getTagById(id);
    }

    @PatchMapping("/{id}")
    public TagDto updateTag(@PathVariable UUID id, @Valid @RequestBody TagUpdateDto dto) {
        return tagService.updateTag(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable UUID id) {
        tagService.deleteTag(id);
    }
}
