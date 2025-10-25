package org.example.bookvexebej2e.services.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.exceptions.ResourceNotFoundException;
import org.example.bookvexebej2e.models.constant.Module;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.db.RoleDbModel;
import org.example.bookvexebej2e.models.db.RolePermissionDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.models.dto.auth.ModulePermission;
import org.example.bookvexebej2e.models.dto.role.RoleCreate;
import org.example.bookvexebej2e.models.dto.role.RolePermissionCreate;
import org.example.bookvexebej2e.models.dto.role.RoleResponse;
import org.example.bookvexebej2e.repositories.role.RoleRepository;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final UserRepository userRepository;
    private final Module moduleConfig = new Module();
    private final RoleRepository roleRepository;

    /**
     * Get module info by code
     */
    private Module.ModuleDto getModuleInfo(String moduleCode) {
        return moduleConfig.getModuleList().stream()
            .filter(m -> m.code.equals(moduleCode))
            .findFirst()
            .orElse(null);
    }

    /**
     * Check if the current authenticated user has the required permission
     */
    public boolean hasPermission(String moduleCode, PermissionAction action) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("No authenticated user found");
                return false;
            }

            // Get username from authentication
            String username = null;
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            }

            if (username == null) {
                log.warn("Could not extract username from authentication");
                return false;
            }

            // Find user and check their permissions
            Optional<UserDbModel> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                log.warn("User {} not found in database", username);
                return false;
            }

            UserDbModel user = userOpt.get();

            // Check if user is admin (bypass permission check)
            if (Boolean.TRUE.equals(user.getIsAdmin())) {
                log.debug("User {} is ADMIN, permission granted", username);
                return true;
            }
            List<RoleUserDbModel> roleUsers = user.getRoleUsers();

            if (roleUsers == null || roleUsers.isEmpty()) {
                log.warn("User {} has no roles assigned", username);
                return false;
            }

            // Check permissions across all user's roles
            for (RoleUserDbModel roleUser : roleUsers) {
                List<RolePermissionDbModel> permissions = roleUser.getRole().getRolePermissions();
                if (permissions != null) {
                    for (RolePermissionDbModel permission : permissions) {
                        if (moduleCode.equals(permission.getModule())) {
                            boolean hasPermission = checkActionPermission(permission, action);
                            if (hasPermission) {
                                log.debug("User {} has {} permission on module {}", username, action, moduleCode);
                                return true;
                            }
                        }
                    }
                }
            }

            log.warn("User {} does not have {} permission on module {}", username, action, moduleCode);
            return false;

        } catch (Exception e) {
            log.error("Error checking permission: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Check if a specific action is allowed in the permission
     */
    private boolean checkActionPermission(RolePermissionDbModel permission, PermissionAction action) {
        return switch (action) {
            case READ -> Boolean.TRUE.equals(permission.getIsCanRead());
            case CREATE -> Boolean.TRUE.equals(permission.getIsCanCreate());
            case UPDATE -> Boolean.TRUE.equals(permission.getIsCanUpdate());
            case DELETE -> Boolean.TRUE.equals(permission.getIsCanDelete());
            case ACTIVATE -> Boolean.TRUE.equals(permission.getIsCanActivate());
            case DEACTIVATE -> Boolean.TRUE.equals(permission.getIsCanDeactivate());
            case IMPORT -> Boolean.TRUE.equals(permission.getIsCanImport());
            case EXPORT -> Boolean.TRUE.equals(permission.getIsCanExport());
        };
    }

    /**
     * Get all permissions for current user on a specific module
     */
    public RolePermissionDbModel getUserPermissionForModule(String moduleCode) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) return null;

            String username = authentication.getName();
            Optional<UserDbModel> userOpt = userRepository.findByUsername(username);

            if (userOpt.isEmpty()) return null;

            UserDbModel user = userOpt.get();
            List<RoleUserDbModel> roleUsers = user.getRoleUsers();

            if (roleUsers == null || roleUsers.isEmpty()) return null;

            // Return the first matching permission (could be enhanced to merge multiple role permissions)
            for (RoleUserDbModel roleUser : roleUsers) {
                List<RolePermissionDbModel> permissions = roleUser.getRole().getRolePermissions();
                if (permissions != null) {
                    for (RolePermissionDbModel permission : permissions) {
                        if (moduleCode.equals(permission.getModule())) {
                            return permission;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting user permission: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Get all permissions for the current authenticated user
     * Returns a map of module code to ModulePermission
     */
    public Map<String, ModulePermission> getAllUserPermissions(UserDbModel user) {
        Map<String, ModulePermission> permissionsMap = new HashMap<>();

        try {
            if (user == null) {
                return permissionsMap;
            }

            // If user is admin, they have all permissions
            if (Boolean.TRUE.equals(user.getIsAdmin())) {
                // You can either return empty map (frontend shows everything for admin)
                // or populate with all modules with full permissions
                return permissionsMap; // Empty map means admin has access to everything
            }

            List<RoleUserDbModel> roleUsers = user.getRoleUsers();
            if (roleUsers == null || roleUsers.isEmpty()) {
                return permissionsMap;
            }

            // Collect all permissions from all roles
            for (RoleUserDbModel roleUser : roleUsers) {
                List<RolePermissionDbModel> permissions = roleUser.getRole().getRolePermissions();
                if (permissions != null) {
                    for (RolePermissionDbModel permission : permissions) {
                        String moduleCode = permission.getModule();

                        // If module already exists, merge permissions (OR logic - more permissive)
                        if (permissionsMap.containsKey(moduleCode)) {
                            ModulePermission existing = permissionsMap.get(moduleCode);
                            existing.setCanRead(existing.getCanRead() || Boolean.TRUE.equals(permission.getIsCanRead()));
                            existing.setCanCreate(existing.getCanCreate() || Boolean.TRUE.equals(permission.getIsCanCreate()));
                            existing.setCanUpdate(existing.getCanUpdate() || Boolean.TRUE.equals(permission.getIsCanUpdate()));
                            existing.setCanDelete(existing.getCanDelete() || Boolean.TRUE.equals(permission.getIsCanDelete()));
                            existing.setCanActivate(existing.getCanActivate() || Boolean.TRUE.equals(permission.getIsCanActivate()));
                            existing.setCanDeactivate(existing.getCanDeactivate() || Boolean.TRUE.equals(permission.getIsCanDeactivate()));
                            existing.setCanImport(existing.getCanImport() || Boolean.TRUE.equals(permission.getIsCanImport()));
                            existing.setCanExport(existing.getCanExport() || Boolean.TRUE.equals(permission.getIsCanExport()));
                        } else {
                            Module.ModuleDto moduleInfo = getModuleInfo(moduleCode);

                            ModulePermission modulePermission = ModulePermission.builder()
                                .moduleCode(moduleCode)
                                .moduleName(moduleInfo != null ? moduleInfo.name : moduleCode)
                                .description(moduleInfo != null ? moduleInfo.description : null)
                                .urlPageView(moduleInfo != null ? moduleInfo.urlPageView : null)
                                .canRead(Boolean.TRUE.equals(permission.getIsCanRead()))
                                .canCreate(Boolean.TRUE.equals(permission.getIsCanCreate()))
                                .canUpdate(Boolean.TRUE.equals(permission.getIsCanUpdate()))
                                .canDelete(Boolean.TRUE.equals(permission.getIsCanDelete()))
                                .canActivate(Boolean.TRUE.equals(permission.getIsCanActivate()))
                                .canDeactivate(Boolean.TRUE.equals(permission.getIsCanDeactivate()))
                                .canImport(Boolean.TRUE.equals(permission.getIsCanImport()))
                                .canExport(Boolean.TRUE.equals(permission.getIsCanExport()))
                                .build();
                            permissionsMap.put(moduleCode, modulePermission);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting all user permissions: {}", e.getMessage(), e);
        }

        return permissionsMap;
    }

    public List<Module.ModuleDto> getAllModule() {
        var module = new Module();
        return module.getModuleList();
    }

    public String assignPermissionToRole(List<RolePermissionCreate> createDtos, UUID roleId) {
        RoleDbModel entity = roleRepository.findByIdAndNotDeleted(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(RoleDbModel.class, roleId));

        // Xóa quyền cũ (Hibernate sẽ orphanRemove đúng cách)
        entity.getRolePermissions().clear();

        // Thêm quyền mới vào collection cũ
        createDtos.forEach(dto -> {
            RolePermissionDbModel perm = new RolePermissionDbModel();
            perm.setModule(dto.getModule());
            perm.setIsCanCreate(dto.getIsCanCreate());
            perm.setIsCanRead(dto.getIsCanRead());
            perm.setIsCanUpdate(dto.getIsCanUpdate());
            perm.setIsCanDelete(dto.getIsCanDelete());
            perm.setIsCanActivate(dto.getIsCanActivate());
            perm.setIsCanDeactivate(dto.getIsCanDeactivate());
            perm.setIsCanImport(dto.getIsCanImport());
            perm.setIsCanExport(dto.getIsCanExport());
            perm.setRole(entity); // bắt buộc
            entity.getRolePermissions().add(perm);
        });

        roleRepository.save(entity);
        return "Success";
    }


    public List<Module.ModuleDto> getByRole(UUID roleId) {
        RoleDbModel entity = roleRepository.findByIdAndNotDeleted(roleId)
                .orElseThrow(() -> new ResourceNotFoundException(RoleDbModel.class, roleId));

        // Lấy danh sách quyền (tránh null)
        List<RolePermissionDbModel> rolePermissions =
                Optional.ofNullable(entity.getRolePermissions()).orElse(Collections.emptyList());

        // Nếu role chưa có quyền nào → trả toàn bộ module với các quyền = false
        if (rolePermissions.isEmpty()) {
            return moduleConfig.getModuleList().stream()
                    .map(m -> {
                        Module.ModuleDto dto = new Module.ModuleDto();
                        dto.code = m.code;
                        dto.name = m.name;
                        dto.description = m.description;
                        dto.urlPageView = m.urlPageView;

                        dto.setIsCanRead(false);
                        dto.setIsCanCreate(false);
                        dto.setIsCanUpdate(false);
                        dto.setIsCanDelete(false);
                        dto.setIsCanActivate(false);
                        dto.setIsCanDeactivate(false);
                        dto.setIsCanImport(false);
                        dto.setIsCanExport(false);

                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        // Nếu role có quyền → ánh xạ từng module
        Map<String, RolePermissionDbModel> permissionMap = rolePermissions.stream()
                .collect(Collectors.toMap(RolePermissionDbModel::getModule, rp -> rp, (a, b) -> a));

        return moduleConfig.getModuleList().stream()
                .map(m -> {
                    Module.ModuleDto dto = new Module.ModuleDto();
                    dto.code = m.code;
                    dto.name = m.name;
                    dto.description = m.description;
                    dto.urlPageView = m.urlPageView;

                    RolePermissionDbModel rp = permissionMap.get(m.code);
                    if (rp != null) {
                        dto.setIsCanRead(rp.getIsCanRead());
                        dto.setIsCanCreate(rp.getIsCanCreate());
                        dto.setIsCanUpdate(rp.getIsCanUpdate());
                        dto.setIsCanDelete(rp.getIsCanDelete());
                        dto.setIsCanActivate(rp.getIsCanActivate());
                        dto.setIsCanDeactivate(rp.getIsCanDeactivate());
                        dto.setIsCanImport(rp.getIsCanImport());
                        dto.setIsCanExport(rp.getIsCanExport());
                    } else {
                        dto.setIsCanRead(false);
                        dto.setIsCanCreate(false);
                        dto.setIsCanUpdate(false);
                        dto.setIsCanDelete(false);
                        dto.setIsCanActivate(false);
                        dto.setIsCanDeactivate(false);
                        dto.setIsCanImport(false);
                        dto.setIsCanExport(false);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }


}

