package ru.practicum.eventhub.application.openapi;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.eventhub.api.dto.response.UserDto;
import ru.practicum.eventhub.api.model.PageOfUsers;
import ru.practicum.eventhub.domain.dto.PagedResponse;
import ru.practicum.eventhub.domain.service.UserService;

@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserService userService;

    public PageOfUsers getUsers(Pageable pageable) {
        PagedResponse<UserDto> page = userService.getUsers(pageable);
        return getPageOfUsers(page);
    }

    private static @NotNull PageOfUsers getPageOfUsers(PagedResponse<UserDto> page) {
        PageOfUsers pageOfUsers = new PageOfUsers();
        pageOfUsers.setPage(page.page());
        pageOfUsers.setSize(page.size());
        pageOfUsers.setTotalElements(page.totalElements());
        pageOfUsers.setTotalPages(page.totalPages());
        pageOfUsers.setContent(page.content());
        return pageOfUsers;
    }
}
