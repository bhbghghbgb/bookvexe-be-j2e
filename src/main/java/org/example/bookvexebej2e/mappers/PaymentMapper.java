package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.PaymentDbModel;
import org.example.bookvexebej2e.models.dto.payment.PaymentResponse;
import org.example.bookvexebej2e.models.dto.payment.PaymentSelectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponse toResponse(PaymentDbModel entity);

    PaymentSelectResponse toSelectResponse(PaymentDbModel entity);


    @AfterMapping
    default void setPermissions(@MappingTarget PaymentResponse response, PaymentDbModel entity) {
        if (response != null && entity != null) {
            response.setIsDeleted(entity.getIsDeleted());
        }
    }
}
