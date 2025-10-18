package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.dto.customer.CustomerResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toResponse(CustomerDbModel entity);

    CustomerSelectResponse toSelectResponse(CustomerDbModel entity);


    @AfterMapping
    default void setPermissions(@MappingTarget CustomerResponse response, CustomerDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
