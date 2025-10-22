package org.example.bookvexebej2e.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.bookvexebej2e.configs.annotations.RequirePermission;
import org.example.bookvexebej2e.models.constant.PermissionAction;
import org.example.bookvexebej2e.services.permission.PermissionService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionAspect {

    private final PermissionService permissionService;

    @Around("@annotation(org.example.bookvexebej2e.configs.annotations.RequirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequirePermission requirePermission = method.getAnnotation(RequirePermission.class);

        if (requirePermission != null) {
            String module = requirePermission.module();
            var action = requirePermission.action();

            log.debug("Checking permission: module={}, action={}", module, action);

            boolean hasPermission = permissionService.hasPermission(module, action);

            if (!hasPermission) {
                log.warn("Access denied: User does not have {} permission on module {}", action, module);
                throw new AccessDeniedException(
                    String.format("Bạn không có quyền %s trên module %s",
                        action.name().toLowerCase(), module)
                );
            }
        }

        return joinPoint.proceed();
    }
}

