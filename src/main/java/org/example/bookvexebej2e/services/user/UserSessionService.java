package org.example.bookvexebej2e.services.user;

import org.example.bookvexebej2e.models.dto.user.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserSessionService {
    List<UserSessionResponse> findAll();

    Page<UserSessionResponse> findAll(UserSessionQuery query);

    UserSessionResponse findById(UUID id);

    UserSessionResponse create(UserSessionCreate createDto);

    UserSessionResponse update(UUID id, UserSessionUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<UserSessionSelectResponse> findAllForSelect();

    Page<UserSessionSelectResponse> findAllForSelect(UserSessionQuery query);
}
