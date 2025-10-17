package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.db.NotificationTypeDbModel;
import org.example.bookvexebej2e.models.dto.notification.NotificationTypeResponse;
import org.example.bookvexebej2e.models.dto.notification.NotificationTypeSelectResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationTypeMapper {

    NotificationTypeResponse toResponse(NotificationTypeDbModel entity);

    NotificationTypeSelectResponse toSelectResponse(NotificationTypeDbModel entity);
}
