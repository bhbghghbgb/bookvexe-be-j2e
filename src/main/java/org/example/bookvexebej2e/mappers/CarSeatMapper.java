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

            // Debug: in ra thông tin booking seats với booking types
            System.out.println("CarSeatMapper - Processing seat: " + entity.getSeatNumber());
            System.out.println("CarSeatMapper - BookingSeats count: "
                    + (entity.getBookingSeats() != null ? entity.getBookingSeats().size() : "null"));

            // Set booking information
            if (entity.getBookingSeats() != null && !entity.getBookingSeats().isEmpty()) {
                // Debug: in ra chi tiết từng booking seat
                for (var bs : entity.getBookingSeats()) {
                    System.out.println("  BookingSeat ID: " + bs.getId() +
                            ", Status: " + bs.getStatus() +
                            ", Booking Type: " + (bs.getBooking() != null ? bs.getBooking().getType() : "null") +
                            ", Booking Status: "
                            + (bs.getBooking() != null ? bs.getBooking().getBookingStatus() : "null"));
                }

                var bookedStatuses = entity.getBookingSeats().stream()
                        .filter(bs -> bs.getStatus() != null && !bs.getStatus().equals("cancelled"))
                        .map(bs -> bs.getStatus())
                        .distinct()
                        .toList();

                System.out.println("CarSeatMapper - Valid (non-cancelled) statuses: " + bookedStatuses);
                response.setBookedStatuses(bookedStatuses);
                response.setIsBooked(!bookedStatuses.isEmpty());
                System.out.println("CarSeatMapper - Set isBooked to: " + !bookedStatuses.isEmpty());
            } else {
                response.setIsBooked(false);
                System.out.println("CarSeatMapper - Set isBooked to: false (no booking seats)");
            }
        }
    }
}
