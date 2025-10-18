package org.example.bookvexebej2e.services.user;

import org.example.bookvexebej2e.models.dto.user.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<UserResponse> findAll();

    Page<UserResponse> findAll(UserQuery query);

    UserResponse findById(UUID id);

    UserResponse create(UserCreate createDto);

    UserResponse update(UUID id, UserUpdate updateDto);

    void delete(UUID id);

    void activate(UUID id);

    void deactivate(UUID id);

    List<UserSelectResponse> findAllForSelect();

    Page<UserSelectResponse> findAllForSelect(UserQuery query);
}
