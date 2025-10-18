package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSelectResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponse toResponse(BookingDbModel entity);

    BookingSelectResponse toSelectResponse(BookingDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget BookingResponse response, BookingDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
