package org.example.bookvexebej2e.services.permission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.models.db.RolePermissionDbModel;
import org.example.bookvexebej2e.models.db.RoleUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.example.bookvexebej2e.repositories.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final UserRepository userRepository;

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
}

