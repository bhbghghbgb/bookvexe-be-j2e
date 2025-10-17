package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.BookingDbModel;
import org.example.bookvexebej2e.models.dto.booking.BookingCreate;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingSelectResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingResponse toResponse(BookingDbModel entity);

    BookingSelectResponse toSelectResponse(BookingDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "bookingSeats", ignore = true)
//    @Mapping(target = "payment", ignore = true)
//    @Mapping(target = "notifications", ignore = true)
    BookingDbModel toEntity(BookingCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "bookingSeats", ignore = true)
//    @Mapping(target = "payment", ignore = true)
//    @Mapping(target = "notifications", ignore = true)
    void updateEntity(BookingUpdate updateDto, @MappingTarget BookingDbModel entity);
}
