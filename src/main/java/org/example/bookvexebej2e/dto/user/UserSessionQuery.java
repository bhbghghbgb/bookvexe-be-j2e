package org.example.bookvexebej2e.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSessionQuery extends BasePageableQuery {
    private UUID userId;
    private Boolean revoked;
}
