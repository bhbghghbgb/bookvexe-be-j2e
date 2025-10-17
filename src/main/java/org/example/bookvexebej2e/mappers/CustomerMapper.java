package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CustomerDbModel;
import org.example.bookvexebej2e.models.dto.customer.CustomerResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerResponse toResponse(CustomerDbModel entity);

    CustomerSelectResponse toSelectResponse(CustomerDbModel entity);

}
