package org.example.bookvexebej2e.services.admin;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;
import org.example.bookvexebej2e.models.responses.RoleCreateUpdateDto;
import org.example.bookvexebej2e.repositories.RoleRepository;
import org.example.bookvexebej2e.repositories.RoleUserRepository;
import org.example.bookvexebej2e.services.admin.base.BaseAdminService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleAdminService extends BaseAdminService<RoleDbModel, Integer, BasePageableQueryRequest> {

    private final RoleRepository roleRepository;
    private final RoleUserRepository roleUserRepository;

    @Override
    protected JpaRepository<RoleDbModel, Integer> getRepository() {
        return roleRepository;
    }

    @Override
    protected Specification<RoleDbModel> buildSpecification(BasePageableQueryRequest request) {
        return (root, query, cb) -> cb.conjunction(); // No specific filtering for roles
    }

    public RoleDbModel create(RoleCreateUpdateDto createDto) {
        if (roleRepository.findByCode(createDto.getCode())
            .isPresent()) {
            throw new RuntimeException("Role code already exists");
        }

        RoleDbModel role = new RoleDbModel();
        role.setCode(createDto.getCode());
        role.setName(createDto.getName());

        return roleRepository.save(role);
    }

    public RoleDbModel update(Integer id, RoleCreateUpdateDto updateDto) {
        RoleDbModel role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found"));

        // Check if code is being changed and if new code already exists
        if (!role.getCode()
            .equals(updateDto.getCode()) && roleRepository.findByCode(updateDto.getCode())
            .isPresent()) {
            throw new RuntimeException("Role code already exists");
        }

        role.setCode(updateDto.getCode());
        role.setName(updateDto.getName());

        return roleRepository.save(role);
    }

    public void delete(Integer id) {
        RoleDbModel role = roleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Role not found"));

        // Check if role is being used by any user
        boolean isRoleAssigned = roleUserRepository.existsByRoleId(id);
        if (isRoleAssigned) {
            throw new RuntimeException("Cannot delete role that is assigned to users");
        }

        roleRepository.delete(role);
    }
}
