package ru.practicum.eventhub.service.impl;

import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.eventhub.config.feign.TagAnalyticsFacade;
import ru.practicum.eventhub.model.ActionType;
import ru.practicum.eventhub.model.CompensationAction;
import ru.practicum.eventhub.repository.CompensationActionRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Server
@RequiredArgsConstructor
public class CompensationScheduler {
    private final CompensationActionRepository repository;
    private final TagAnalyticsFacade analyticsFacade;

    @Scheduled(fixedDelay = 180000)
    @Transactional
    public void processCompensations() {
        List<CompensationAction> actions = repository.findAll();

        for (CompensationAction action : actions) {
            try {
                if (action.getActionType() == ActionType.DELETE_ANALYTICS) {
                    UUID tagId = UUID.fromString(action.getPayload());
                    analyticsFacade.deleteTagAnalytics(tagId);
                }

                repository.delete(action);
                log.info("Успешно выполнена компенсация для id={}", action.getId());

            } catch (Exception exception) {
                action.setRetries(action.getRetries() + 1);
                repository.save(action);
                log.warn("Повторная ошибка компенсации для id={}. Попытка №{}", action.getId(), action.getRetries());
            }
        }
    }
}
