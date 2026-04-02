package ru.practicum.eventhub.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.model.ActionType;
import ru.practicum.eventhub.model.CompensationAction;
import ru.practicum.eventhub.repository.CompensationActionRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompensationService {
    private final CompensationActionRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveForLater(UUID tagId, ActionType type) {
        CompensationAction action = new CompensationAction();
        action.setPayload(tagId.toString());
        action.setActionType(type);
        repository.save(action);
    }
}
