package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.dto.notification.NotificationTypeCreate;
import org.example.bookvexebej2e.dto.notification.NotificationTypeResponse;
import org.example.bookvexebej2e.dto.notification.NotificationTypeSelectResponse;
import org.example.bookvexebej2e.dto.notification.NotificationTypeUpdate;
import org.example.bookvexebej2e.models.db.NotificationTypeDbModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NotificationTypeMapper {

    NotificationTypeResponse toResponse(NotificationTypeDbModel entity);

    NotificationTypeSelectResponse toSelectResponse(NotificationTypeDbModel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    NotificationTypeDbModel toEntity(NotificationTypeCreate createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "notifications", ignore = true)
    void updateEntity(NotificationTypeUpdate updateDto, @MappingTarget NotificationTypeDbModel entity);
}
