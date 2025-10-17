package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.dto.payment.PaymentResponse;
import org.example.bookvexebej2e.models.dto.payment.PaymentSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponse toResponse(PaymentDbModel entity);

    PaymentSelectResponse toSelectResponse(PaymentDbModel entity);

}
