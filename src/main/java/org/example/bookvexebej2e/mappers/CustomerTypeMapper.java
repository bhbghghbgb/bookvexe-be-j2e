package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CustomerTypeDbModel;
import org.example.bookvexebej2e.models.dto.customer.CustomerTypeResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerTypeSelectResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerTypeMapper {

    CustomerTypeResponse toResponse(CustomerTypeDbModel entity);

    CustomerTypeSelectResponse toSelectResponse(CustomerTypeDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget CustomerTypeResponse response, CustomerTypeDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
