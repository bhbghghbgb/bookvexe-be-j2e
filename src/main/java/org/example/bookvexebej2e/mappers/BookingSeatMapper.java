package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSeatSelectResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingSeatMapper {
    BookingSeatResponse toResponse(BookingSeatDbModel entity);

    BookingSeatSelectResponse toSelectResponse(BookingSeatDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget BookingSeatResponse response, BookingSeatDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
