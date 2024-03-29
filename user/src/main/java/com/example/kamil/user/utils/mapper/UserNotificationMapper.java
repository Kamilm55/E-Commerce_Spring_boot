package com.example.kamil.user.utils.mapper;

import com.example.kamil.user.model.dto.UserNotificationDTO;
import com.example.kamil.user.model.entity.UserNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserNotificationMapper {
    UserNotificationMapper INSTANCE = Mappers.getMapper( UserNotificationMapper.class );
    @Mapping(target="email", source="userDetails.email")
    UserNotificationDTO convertToDTO(UserNotification userNotification);
}

