package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.CarSeatDbModel;
import org.example.bookvexebej2e.models.dto.car.CarSeatResponse;
import org.example.bookvexebej2e.models.dto.car.CarSeatSelectResponse;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CarSeatMapper {

    CarSeatResponse toResponse(CarSeatDbModel entity);

    CarSeatSelectResponse toSelectResponse(CarSeatDbModel entity);

    @AfterMapping
    default void setPermissions(@MappingTarget CarSeatResponse response, CarSeatDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
            if (entity.getBookingSeats() != null && !entity.getBookingSeats().isEmpty()) {
                var bookedStatuses = entity.getBookingSeats().stream()
                        .filter(bs -> bs.getStatus() != null && !bs.getStatus().equals("cancelled"))
                        .map(bs -> bs.getStatus())
                        .distinct()
                        .toList();
                response.setBookedStatuses(bookedStatuses);
                response.setIsBooked(!bookedStatuses.isEmpty());
            } else {
                response.setIsBooked(false);
            }
        }
    }
}
