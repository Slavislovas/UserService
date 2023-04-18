package com.fitnessapp.userservice.business.service;

import com.fitnessapp.userservice.model.UserCreationDto;
import com.fitnessapp.userservice.model.UserDto;
import com.fitnessapp.userservice.model.UserEditDto;

import java.util.List;

public interface UserService {
    UserDto findUserById(String userId);

    UserDto findByUsername(String username);

    List<UserDto> findAllUsers();

    UserDto createUser(UserCreationDto userCreationDto);

    UserDto editUser(String id, UserEditDto userCreationDto);
}
