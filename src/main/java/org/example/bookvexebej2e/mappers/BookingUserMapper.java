package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSelectResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingUserMapper {
    BookingResponse toResponse(BookingDbModel entity);

    default BookingSelectResponse toSelectResponse(BookingDbModel entity) {
        if (entity == null)
            return null;

        BookingSelectResponse dto = new BookingSelectResponse();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setType(entity.getType());

        if (entity.getCustomer() != null) {
            dto.setCustomerName(entity.getCustomer().getName());
        }
        return dto;
    };

    @AfterMapping
    default void setPermissions(@MappingTarget BookingResponse response, BookingDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}