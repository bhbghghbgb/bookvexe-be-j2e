package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CustomerTypeDbModel;
import org.example.bookvexebej2e.models.dto.customer.CustomerTypeResponse;
import org.example.bookvexebej2e.models.dto.customer.CustomerTypeSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerTypeMapper {

    CustomerTypeResponse toResponse(CustomerTypeDbModel entity);

    CustomerTypeSelectResponse toSelectResponse(CustomerTypeDbModel entity);
}
