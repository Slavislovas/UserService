package com.fitnessapp.userservice.business.service;

import com.fitnessapp.userservice.model.UserCreationDto;
import com.fitnessapp.userservice.model.UserDto;

import java.util.List;

public interface UserService {
    UserDto findUserById(String userId);

    UserDto findByUsername(String username);

    List<UserDto> findAllUsers();

    UserDto createUser(UserCreationDto userCreationDto);
}
