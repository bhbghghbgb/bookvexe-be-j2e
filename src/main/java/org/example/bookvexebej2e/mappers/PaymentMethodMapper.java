package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.PaymentMethodDbModel;
import org.example.bookvexebej2e.models.dto.payment.PaymentMethodCreate;
import org.example.bookvexebej2e.models.dto.payment.PaymentMethodResponse;
import org.example.bookvexebej2e.models.dto.payment.PaymentMethodSelectResponse;
import org.example.bookvexebej2e.models.dto.payment.PaymentMethodUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodResponse toResponse(PaymentMethodDbModel entity);

    PaymentMethodSelectResponse toSelectResponse(PaymentMethodDbModel entity);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "payments", ignore = true)
    PaymentMethodDbModel toEntity(PaymentMethodCreate createDto);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "isDeleted", ignore = true)
//    @Mapping(target = "createdDate", ignore = true)
//    @Mapping(target = "createdBy", ignore = true)
//    @Mapping(target = "updatedDate", ignore = true)
//    @Mapping(target = "updatedBy", ignore = true)
//    @Mapping(target = "payments", ignore = true)
    void updateEntity(PaymentMethodUpdate updateDto, @MappingTarget PaymentMethodDbModel entity);
}
