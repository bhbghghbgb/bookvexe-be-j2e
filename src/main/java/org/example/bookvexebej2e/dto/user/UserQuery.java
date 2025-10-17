package org.example.bookvexebej2e.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.dto.base.BasePageableQuery;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery extends BasePageableQuery {
    private String username;
    private String email;
    private Boolean isGoogle;
}
