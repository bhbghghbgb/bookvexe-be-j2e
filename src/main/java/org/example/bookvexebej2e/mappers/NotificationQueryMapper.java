package org.example.bookvexebej2e.mappers;

import org.example.bookvexebej2e.models.dto.notification.NotificationQuery;
import org.example.bookvexebej2e.models.dto.notification.NotificationUserQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.UUID;

@Mapper(componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NotificationQueryMapper {

    /**
     * Maps the NotificationUserQuery to NotificationQuery, allowing the userId to be set separately.
     * All fields are mapped, except the userId which is handled by the security context.
     */
    @Mapping(target = "userId", ignore = true)
    NotificationQuery toNotificationQuery(NotificationUserQuery userQuery);

    /**
     * Completes the mapping by setting the required userId.
     */
    default NotificationQuery toNotificationQueryWithUser(NotificationUserQuery userQuery, UUID userId) {
        NotificationQuery query = toNotificationQuery(userQuery);
        query.setUserId(userId);
        return query;
    }
}