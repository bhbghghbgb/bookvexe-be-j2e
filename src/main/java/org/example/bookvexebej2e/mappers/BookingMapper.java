package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingResponse toResponse(BookingDbModel entity);

    BookingSelectResponse toSelectResponse(BookingDbModel entity);
}
