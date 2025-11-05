package com.bookvexe.notificationservice.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSessionQuery extends BasePageableQuery {
    private UUID userId;
    private Boolean revoked;
}
