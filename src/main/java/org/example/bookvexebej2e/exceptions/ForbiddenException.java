package org.example.bookvexebej2e.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@Getter
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    private final Set<String> requiredRoles;
    private final Set<String> userRoles;

    public ForbiddenException(Set<String> requiredRoles, Set<String> userRoles) {
        super("Access denied. Required roles: " + requiredRoles + ", User roles: " + userRoles);
        this.requiredRoles = requiredRoles;
        this.userRoles = userRoles;
    }

}
