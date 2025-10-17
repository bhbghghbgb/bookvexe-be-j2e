package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.notification.NotificationCreate;
import org.example.bookvexebej2e.dto.notification.NotificationResponse;
import org.example.bookvexebej2e.dto.notification.NotificationSelectResponse;
import org.example.bookvexebej2e.dto.notification.NotificationUpdate;
import org.example.bookvexebej2e.models.db.NotificationDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationResponse toResponse(NotificationDbModel entity);

    NotificationSelectResponse toSelectResponse(NotificationDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    NotificationDbModel toEntity(NotificationCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntity(NotificationUpdate updateDto, @MappingTarget NotificationDbModel entity);
}
