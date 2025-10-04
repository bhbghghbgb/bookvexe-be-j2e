package org.example.bookvexebej2e.services.admin;

import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.requests.UserQueryRequest;
import org.example.bookvexebej2e.repositories.RoleUserRepository;
import org.example.bookvexebej2e.repositories.UserRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdminService extends BaseAdminService<UserDbModel, Integer, UserQueryRequest> {

    private final UserRepository userRepository;
    private final RoleUserRepository roleUserRepository;

    @Override
    protected JpaRepository<UserDbModel, Integer> getRepository() {
        return userRepository;
    }

    @Override
    protected Specification<UserDbModel> buildSpecification(UserQueryRequest request) {
        return (root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();

            // Filter by name (partial match, case insensitive)
            if (StringUtils.hasText(request.getName())) {
                predicates.add(cb.like(
                    cb.lower(root.get("fullName")),
                    "%" + request.getName().toLowerCase() + "%"
                ));
            }

            // Filter by email (partial match, case insensitive)
            if (StringUtils.hasText(request.getEmail())) {
                predicates.add(cb.like(
                    cb.lower(root.get("email")),
                    "%" + request.getEmail().toLowerCase() + "%"
                ));
            }

            // Filter by active status
            if (request.getActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), request.getActive()));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

    /**
     * Lấy danh sách roles của user
     */
    public List<RoleUserDbModel> getUserRoles(Integer userId) {
        return userRepository.findById(userId)
            .map(roleUserRepository::findByUser)
            .orElse(Collections.emptyList());
    }
}