package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.booking.BookingSeatCreate;
import org.example.bookvexebej2e.dto.booking.BookingSeatResponse;
import org.example.bookvexebej2e.dto.booking.BookingSeatSelectResponse;
import org.example.bookvexebej2e.dto.booking.BookingSeatUpdate;
import org.example.bookvexebej2e.models.db.BookingSeatDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingSeatMapper {

    BookingSeatResponse toResponse(BookingSeatDbModel entity);

    BookingSeatSelectResponse toSelectResponse(BookingSeatDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    BookingSeatDbModel toEntity(BookingSeatCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(BookingSeatUpdate updateDto, @MappingTarget BookingSeatDbModel entity);
}
