package com.bookvexe.notificationservice.dto.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class RoleUserQuery extends BasePageableQuery {
    private UUID roleId;
    private UUID userId;
}
