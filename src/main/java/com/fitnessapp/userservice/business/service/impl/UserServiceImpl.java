package com.fitnessapp.userservice.business.service.impl;

import com.fitnessapp.userservice.business.mapper.UserMapStruct;
import com.fitnessapp.userservice.business.repository.UserRepository;
import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.business.service.UserService;
import com.fitnessapp.userservice.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapStruct userMapStruct;

    @Override
    public UserDto findUserById(Long userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()){
            throw new NoSuchElementException("User with id: " + userId + " does not exist");
        }
        return userMapStruct.entityToDto(userEntityOptional.get());
    }

    @Override
    public UserDto findByUsername(String username) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        if (userEntityOptional.isEmpty()){
            throw new NoSuchElementException("User with usename: " + username + " does not exist");
        }
        return userMapStruct.entityToDto(userEntityOptional.get());
    }
}
