package com.fitnessapp.userservice.business.mapper;

import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.model.UserCreationDto;
import com.fitnessapp.userservice.model.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapStruct {
    UserEntity creationDtoToEntity(UserCreationDto creationDto);
    UserEntity dtoToEntity(UserDto dto);
    UserDto entityToDto(UserEntity entity);
}
