package com.bookvexe.notificationservice.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends BasePageableQuery {
    private String username;
    private String email;
    private Boolean isGoogle;
}
