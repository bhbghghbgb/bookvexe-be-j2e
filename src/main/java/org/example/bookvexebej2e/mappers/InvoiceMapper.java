package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.InvoiceDbModel;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceResponse;
import org.example.bookvexebej2e.models.dto.invoice.InvoiceSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    InvoiceResponse toResponse(InvoiceDbModel entity);

    InvoiceSelectResponse toSelectResponse(InvoiceDbModel entity);

}
