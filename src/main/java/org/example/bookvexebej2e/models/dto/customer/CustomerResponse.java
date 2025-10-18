package org.example.bookvexebej2e.models.dto.customer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.bookvexebej2e.models.dto.base.BasePermissionResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerResponse extends BasePermissionResponse {
    private UUID id;
    private String code;
    private String name;
    private String email;
    private String phone;
    private CustomerTypeResponse customerType;
    private String description;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Boolean isDeleted;

    public CustomerResponse() {
        super();
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        setPermissionsByDeletedStatus(isDeleted);
    }
}
