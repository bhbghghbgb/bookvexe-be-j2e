package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.InvoiceDbModel;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceResponse toResponse(InvoiceDbModel entity);

    InvoiceSelectResponse toSelectResponse(InvoiceDbModel entity);


    @AfterMapping
    default void setPermissions(@MappingTarget InvoiceResponse response, InvoiceDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
