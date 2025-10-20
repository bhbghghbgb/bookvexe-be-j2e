package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.PaymentMethodDbModel;
import org.example.bookvexebej2e.models.dto.payment.PaymentMethodResponse;
import org.example.bookvexebej2e.models.dto.payment.PaymentMethodSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodResponse toResponse(PaymentMethodDbModel entity);

    PaymentMethodSelectResponse toSelectResponse(PaymentMethodDbModel entity);
}
