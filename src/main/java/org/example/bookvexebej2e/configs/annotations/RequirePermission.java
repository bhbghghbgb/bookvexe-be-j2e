package org.example.bookvexebej2e.configs.annotations;

import org.example.bookvexebej2e.models.constant.PermissionAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to check if user has specific permission on a module
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    /**
     * Module code (e.g., CUSTOMER, USER, VEHICLE)
     */
    String module();

    /**
     * Required action (READ, CREATE, UPDATE, DELETE, etc.)
     */
    PermissionAction action();
}

