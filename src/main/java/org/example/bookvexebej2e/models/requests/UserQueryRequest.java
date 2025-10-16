package org.example.bookvexebej2e.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bookvexebej2e.models.requests.base.BasePageableQueryRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryRequest extends BasePageableQueryRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String[] includeRoles;
    private String[] excludeRoles;
    private Boolean active;
}

