package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.invoice.InvoiceCreate;
import org.example.bookvexebej2e.dto.invoice.InvoiceResponse;
import org.example.bookvexebej2e.dto.invoice.InvoiceSelectResponse;
import org.example.bookvexebej2e.dto.invoice.InvoiceUpdate;
import org.example.bookvexebej2e.models.db.InvoiceDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceResponse toResponse(InvoiceDbModel entity);

    InvoiceSelectResponse toSelectResponse(InvoiceDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    InvoiceDbModel toEntity(InvoiceCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(InvoiceUpdate updateDto, @MappingTarget InvoiceDbModel entity);
}
