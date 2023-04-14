package com.fitnessapp.userservice.business.service;

import com.fitnessapp.userservice.model.UserDto;

public interface UserService {
    UserDto findUserById(Long userId);

    UserDto findByUsername(String username);
}
