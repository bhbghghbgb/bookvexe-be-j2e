package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.requests.UserQueryRequest;
import org.example.bookvexebej2e.repositories.RoleUserRepository;
import org.example.bookvexebej2e.repositories.UserRepository;
import org.example.bookvexebej2e.repositories.UserRepositoryCustom;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdminService extends BaseAdminService<UserDbModel, Integer> {

    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;
    private final RoleUserRepository roleUserRepository;

    @Override
    protected JpaRepository<UserDbModel, Integer> getRepository() {
        return userRepository;
    }

    public Page<UserDbModel> findUsersByCriteria(UserQueryRequest queryRequest) {
        return userRepositoryCustom.findUsersByCriteria(queryRequest.getName(), queryRequest.getEmail(),
            queryRequest.getActive(), queryRequest.toPageable());
    }

    public List<UserDbModel> findUsersWithActiveRoles() {
        return userRepositoryCustom.findUsersWithActiveRoles();
    }

    public Page<UserDbModel> findActiveUsersByName(String name, Pageable pageable) {
        return userRepository.findByFullNameContainingIgnoreCaseAndIsActiveTrue(name, pageable);
    }

    public List<RoleUserDbModel> getUserRoles(Integer userId) {
        return userRepository.findById(userId)
            .map(roleUserRepository::findByUser)
            .orElse(Collections.emptyList());
    }
}