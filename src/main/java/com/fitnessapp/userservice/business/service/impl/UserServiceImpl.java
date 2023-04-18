package com.fitnessapp.userservice.business.service.impl;

import com.fitnessapp.userservice.business.enumerator.Role;
import com.fitnessapp.userservice.business.handler.FormErrorModel;
import com.fitnessapp.userservice.business.handler.exception.DuplicateDataException;
import com.fitnessapp.userservice.business.mapper.UserMapStruct;
import com.fitnessapp.userservice.business.repository.UserRepository;
import com.fitnessapp.userservice.business.repository.model.UserEntity;
import com.fitnessapp.userservice.business.service.UserService;
import com.fitnessapp.userservice.model.UserCreationDto;
import com.fitnessapp.userservice.model.UserDto;
import com.fitnessapp.userservice.model.UserEditDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapStruct userMapStruct;

    @Override
    public UserDto findUserById(String userId) {
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

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream().map(userMapStruct::entityToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(UserCreationDto userCreationDto) {
        String dtoUsername = userCreationDto.getUsername();
        String dtoEmail = userCreationDto.getEmail();
        checkForDuplicateData(dtoUsername, dtoEmail);
        UserEntity userEntity = userMapStruct.creationDtoToEntity(userCreationDto);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole(Role.ROLE_USER);
        return userMapStruct.entityToDto(userRepository.save(userEntity));
    }

    @Override
    public UserDto editUser(String id, UserEditDto userEditDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isEmpty()){
            throw new NoSuchElementException("User with id: " + id +" does not exist");
        }
        UserEntity user = optionalUserEntity.get();
        if (!user.getEmail().equals(userEditDto.getEmail())){
            checkForDuplicateData(null, userEditDto.getEmail());
        }
        user.setName(userEditDto.getName());
        user.setSurname(userEditDto.getSurname());
        user.setDateOfBirth(userEditDto.getDateOfBirth());
        user.setEmail(userEditDto.getEmail());
        return userMapStruct.entityToDto(userRepository.save(user));

    }

    private void checkForDuplicateData(String dtoUsername, String dtoEmail) {
        FormErrorModel formErrorModel = new FormErrorModel();
        if (dtoUsername != null && userRepository.findByUsername(dtoUsername).isPresent()){
            formErrorModel.addFormError("username", "username is taken");
        }

        if (dtoEmail != null && userRepository.findByEmail(dtoEmail).isPresent()){
            formErrorModel.addFormError("email", "email is taken");
        }

        if (formErrorModel.getErrors().size() > 0){
            throw new DuplicateDataException(formErrorModel);
        }
    }
}
