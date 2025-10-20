package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingSeatMapper {

    BookingSeatResponse toResponse(BookingSeatDbModel entity);

    BookingSeatSelectResponse toSelectResponse(BookingSeatDbModel entity);
}
