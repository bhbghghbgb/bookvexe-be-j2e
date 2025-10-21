package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.db.BookingDbModel;  // Assume this exists
import org.example.bookvexebej2e.models.db.InvoiceDbModel;  // Assume this exists
import org.example.bookvexebej2e.models.dto.payment.PaymentResponse;
import org.example.bookvexebej2e.models.dto.payment.PaymentSelectResponse;
import org.example.bookvexebej2e.models.dto.booking.BookingResponse;  // Assume this exists
import org.example.bookvexebej2e.models.dto.invoice.InvoiceResponse;  // Assume this exists
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    // Map Payment without recursing into back-refs
    @Mapping(target = "booking", source = "booking", qualifiedByName = "mapBookingShallow")
    @Mapping(target = "invoice", source = "invoice", qualifiedByName = "mapInvoiceShallow")
    PaymentResponse toResponse(PaymentDbModel entity);

    PaymentSelectResponse toSelectResponse(PaymentDbModel entity);

    // Shallow mapping for Booking (ignores payment back-ref)
    @Named("mapBookingShallow")
    @Mapping(target = "payment", ignore = true)  // Break cycle here
    BookingResponse bookingDbModelToBookingResponse(BookingDbModel booking);

    // Shallow mapping for Invoice (ignores payment back-ref)
    @Named("mapInvoiceShallow")
    @Mapping(target = "payment", ignore = true)  // Break cycle here
    InvoiceResponse invoiceDbModelToInvoiceResponse(InvoiceDbModel invoice);

    @AfterMapping
    default void setPermissions(@MappingTarget PaymentResponse response, PaymentDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}